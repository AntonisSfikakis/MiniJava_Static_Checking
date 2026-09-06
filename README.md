# MiniJava Static Checking (Semantic Analysis)

A semantic analyzer and type checker for MiniJava — a statically-typed subset of Java — built around a JavaCC/JTB front-end. Covers full type checking, inheritance-aware override/overload resolution, and field/method offset computation with inheritance-aware memory layout. Originally built for a university compilers course; validated at 10/10 against a hidden test suite (see **Grade** below).

**Antonis Sfikakis**

---

## How to run

```
make
cd build
java Main <file1.java> <file2.java> ...
```

The program accepts one or more MiniJava files as arguments. For each file, it performs parsing, semantic analysis, and offset calculation. If a file contains a semantic error, an error message is printed; if it's valid, the offsets of every class's fields and methods are printed.

For a clean build:

```
make clean
make
```

---

## Project structure

```
├── Makefile
├── minijava.jj                    # MiniJava grammar, in JavaCC form
├── lib/
│   ├── jtb133di.jar               # Java Tree Builder
│   └── javacc5.jar                # JavaCC Parser Generator
├── src/
│   ├── Main.java                  # Entry point — orchestration
│   ├── SpyVisitor.java            # Visitor 1: Symbol Table Construction
│   ├── SemAnalysisVisitor.java    # Visitor 2: Semantic Analysis / Type Checking
│   ├── OffsetCalculator.java      # Offset computation and printing
│   └── ClassInfo.java             # Data structures (ClassInfo, MethodInfo)
├── generated/                     # Auto-generated files
│   ├── minijava-jtb.jj            # Enriched grammar (JTB output)
│   ├── syntaxtree/                # AST node classes (JTB output)
│   ├── visitor/                   # Visitor interfaces + DepthFirst (JTB output)
│   └── parser/                    # Parser + lexer (JavaCC output)
└── build/                         # Compiled .class files
```

---

## Toolchain

The build pipeline follows these steps:

1. **JTB** — Takes the grammar (`minijava.jj`) and produces the enriched `.jj` file, the AST node classes (`syntaxtree/`), and the visitor interfaces (`visitor/`).
2. **JavaCC** — Takes the enriched `.jj` file and generates the parser and lexer in Java (`parser/`).
3. **javac** — Compiles everything (generated code + `src/`) into `.class` files.

---

## Architecture

The work is split into three main stages:

### 1. SpyVisitor — Symbol Table Construction

The first visitor walks the AST and collects information about every class, storing it in a `LinkedHashMap<String, ClassInfo>`. Using a `LinkedHashMap` preserves declaration order, which is required for correct offset calculation later.

**ClassInfo** — represents a class and holds:

- `LinkedHashMap<String, String> Field` — the class's fields (name → type)
- `LinkedHashMap<String, List<MethodInfo>> Methods` — the methods (name → list of `MethodInfo`, a list to support overloading)
- `String Parent_class` — the parent class (`null` if there's no `extends`)

**MethodInfo** — represents a method and holds:

- `String Return_type` — the return type
- `LinkedHashMap<String, String> Parameters` — parameters, in order (name → type)
- `HashMap<String, String> Local_vars` — local variables (name → type)

`SpyVisitor` also performs the following checks while collecting this information:

- Duplicate class names
- Duplicate field names within the same class
- Duplicate parameter names within the same method
- Duplicate local variable names within the same method
- A parent class named in an `extends` must already be defined

### 2. SemAnalysisVisitor — Semantic Analysis & Type Checking

The second visitor walks the AST again, this time using the symbol table built by the first. It performs the following checks:

**Inheritance:**

- Circular inheritance detection (`A extends B`, `B extends A`)

**Type checking on expressions:**

- Arithmetic operations (`+`, `-`, `*`): require `int` operands, return `int`
- Comparison (`<`): requires `int` operands, returns `boolean`
- Logical AND (`&&`): requires `boolean` operands, returns `boolean`
- Logical negation (`!`): requires a `boolean` operand, returns `boolean`
- Array lookup (`a[i]`): requires `int[]` and an `int` index, returns `int`
- Array length (`a.length`): requires `int[]`, returns `int`
- Array allocation (`new int[expr]`): requires an `int` size, returns `int[]`
- Object allocation (`new A()`): checks the class exists, returns its type

**Statements:**

- Assignment: the right-hand type must be compatible with the left-hand type (subtyping is supported)
- Array assignment: checks `int[]`, `int` index, `int` value
- If/While: the condition must be `boolean`
- Print: the expression must be `int`

**Method calls (MessageSend):**

- The receiver must be a class type (not a primitive)
- The method must exist in the class or in one of its ancestors
- Argument types must match the parameters (subtyping is supported)
- Method lookup spans the entire inheritance chain (via `GetAllMethods`)

**Variable lookup:**

- Variables are resolved in order: local vars → parameters → fields → parent fields
- A local variable shadows a field of the same name

**Return type:**

- The type of the return expression must be compatible with the declared return type

**Override / overload:**

- **Override:** a method in a subclass with the same name and parameter types as one in the parent must have the same return type
- **Overload:** methods sharing a name must be unambiguously distinguishable — at least one parameter position must have no subtype/supertype relationship between the two signatures
- These checks apply both within the same class and across parent/child classes

**Helper methods:**

- `LookupType(var)` — resolves a variable's type (local → params → fields → parent fields)
- `LookupFather(class, father)` — checks whether a class is a subtype of another
- `GetAllMethods(class, method)` — collects every method with a given name across the whole inheritance chain
- `isCompatible(from, to)` — checks whether two types are compatible (same, or subtype)
- `IsPrimitive(type)` — checks whether a type is primitive (`int`, `boolean`, `int[]`)
- `CheckOverride(method)` — validates correct method overriding
- `CheckOverload(method)` — ensures there's no ambiguous overloading

**The `isVariable` mechanism:** the `isVariable` flag distinguishes when an `Identifier` represents a variable (needs a type lookup) versus when it represents a class or method name (returned as a plain string). It's turned on in `visit(PrimaryExpression)` and turned off at points such as `AllocationExpression` and `MethodDeclaration`, where identifiers are not variables.

### 3. OffsetCalculator — Offset Calculation

Computes and prints the field/method offsets for every class (the `MainClass` is excluded).

**Type sizes:**

- `int` → 4 bytes
- `boolean` → 1 byte
- Pointers (class types, `int[]`) → 8 bytes
- Methods → 8 bytes (function pointer)

**Rules:**

- Classes without `extends`: offsets start at 0
- Classes with `extends`: offsets continue from where the parent left off
- Overridden methods aren't printed (they keep the parent's offset)
- Overloaded methods are printed separately, with a type suffix (e.g. `foo`, `foo_int`, `foo_int_int`)

**Implementation:**

- `HashMap<String, Pair> class_offsets` — stores each class's final offsets (field offset, method offset) for its children to build on
- `HashMap<String, Set<String>> methodSignatures` — stores method signatures per class to detect overrides. Signatures are inherited from the parent, and if a new method signature already exists in the set, it's recognized as an override and skipped

---

## Notes

- `MainClass` is handled specially: a `MethodInfo` is created for `main` so that semantic checking still applies to its body, but it doesn't appear in the offset output.
- Overloaded methods sharing a name are grouped under the same key in the `LinkedHashMap`, so the print order can differ slightly from declaration order (confirmed acceptable by the course staff).
- Generated files (JTB + JavaCC output) live under `generated/` and are produced automatically by `make`.

## Grade

10/10 — Compilers, University of Athens (EKPA)
