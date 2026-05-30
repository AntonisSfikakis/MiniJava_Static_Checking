# MiniJava Static Checking (Semantic Analysis)
### Εργασία 2 — Μεταγλωττιστές
**Αντώνιος Σφηκάκης — sdi2200178**

---

## Πώς να τρέξετε το πρόγραμμα

```bash
make
cd build
java Main <αρχείο1.java> <αρχείο2.java> ...
```

Το πρόγραμμα δέχεται ένα ή περισσότερα αρχεία MiniJava ως ορίσματα. Για κάθε αρχείο εκτελεί parsing, semantic analysis και υπολογισμό offsets. Αν ένα αρχείο περιέχει σημασιολογικό σφάλμα, τυπώνεται μήνυμα λάθους. Αν είναι σωστό, τυπώνονται τα offsets των fields και methods κάθε κλάσης.

Για καθαρό build:
```bash
make clean
make
```

---

## Δομή του Project

```
├── Makefile
├── minijava.jj                    # Η γραμματική MiniJava σε μορφή JavaCC
├── lib/
│   ├── jtb133di.jar               # Java Tree Builder
│   └── javacc5.jar                # JavaCC Parser Generator
├── src/
│   ├── Main.java                  # Entry point — orchestration
│   ├── SpyVisitor.java            # Visitor 1: Symbol Table Construction
│   ├── SemAnalysisVisitor.java    # Visitor 2: Semantic Analysis / Type Checking
│   ├── OffsetCalculator.java      # Υπολογισμός και εκτύπωση offsets
│   └── ClassInfo.java             # Δομές δεδομένων (ClassInfo, MethodInfo)
├── generated/                     # Αυτόματα παραγόμενα αρχεία
│   ├── minijava-jtb.jj            # Εμπλουτισμένη γραμματική (JTB output)
│   ├── syntaxtree/                # AST node κλάσεις (JTB output)
│   ├── visitor/                   # Visitor interfaces + DepthFirst (JTB output)
│   └── parser/                    # Parser + Lexer (JavaCC output)
└── build/                         # Compiled .class αρχεία
```

---

## Αλυσίδα Εργαλείων

Η αλυσίδα build ακολουθεί τα εξής βήματα:

1. **JTB** — Παίρνει τη γραμματική (`minijava.jj`) και παράγει το εμπλουτισμένο `.jj`, τις AST node κλάσεις (`syntaxtree/`) και τα Visitor interfaces (`visitor/`).
2. **JavaCC** — Παίρνει το εμπλουτισμένο `.jj` και παράγει τον parser και lexer σε Java (`parser/`).
3. **javac** — Μεταγλωττίζει τα πάντα (generated + src) σε `.class` αρχεία.

---

## Αρχιτεκτονική

Η εργασία χωρίζεται σε τρία βασικά στάδια:

### 1. SpyVisitor — Symbol Table Construction

Ο πρώτος visitor διασχίζει το AST και συλλέγει πληροφορίες για κάθε κλάση, αποθηκεύοντάς τες σε δομή `LinkedHashMap<String, ClassInfo>`. Η χρήση LinkedHashMap εξασφαλίζει τη διατήρηση της σειράς δήλωσης, κάτι απαραίτητο για τον σωστό υπολογισμό offsets.

**ClassInfo** — Αναπαριστά μία κλάση και περιέχει:
- `LinkedHashMap<String, String> Field` — Τα πεδία της κλάσης (όνομα → τύπος)
- `LinkedHashMap<String, List<MethodInfo>> Methods` — Οι μέθοδοι (όνομα → λίστα MethodInfo, λίστα για υποστήριξη overloading)
- `String Parent_class` — Η γονική κλάση (null αν δεν κάνει extends)

**MethodInfo** — Αναπαριστά μία μέθοδο και περιέχει:
- `String Return_type` — Τύπος επιστροφής
- `LinkedHashMap<String, String> Parameters` — Παράμετροι με σειρά (όνομα → τύπος)
- `HashMap<String, String> Local_vars` — Τοπικές μεταβλητές (όνομα → τύπος)

Ο SpyVisitor εκτελεί επίσης τους εξής ελέγχους κατά τη συλλογή:
- Duplicate class names
- Duplicate field names μέσα στην ίδια κλάση
- Duplicate parameter names μέσα στην ίδια μέθοδο
- Duplicate local variable names μέσα στην ίδια μέθοδο
- Η γονική κλάση σε extends πρέπει να έχει οριστεί πριν

### 2. SemAnalysisVisitor — Semantic Analysis & Type Checking

Ο δεύτερος visitor διασχίζει ξανά το AST χρησιμοποιώντας το symbol table που κατασκεύασε ο πρώτος. Εκτελεί τους εξής ελέγχους:

**Κληρονομικότητα (Inheritance):**
- Circular inheritance detection (A extends B, B extends A)

**Type Checking σε Expressions:**
- Αριθμητικές πράξεις (`+`, `-`, `*`): απαιτούν `int` operands, επιστρέφουν `int`
- Σύγκριση (`<`): απαιτεί `int` operands, επιστρέφει `boolean`
- Λογικές πράξεις (`&&`): απαιτεί `boolean` operands, επιστρέφει `boolean`
- Λογική άρνηση (`!`): απαιτεί `boolean` operand, επιστρέφει `boolean`
- Array lookup (`a[i]`): απαιτεί `int[]` και `int` index, επιστρέφει `int`
- Array length (`a.length`): απαιτεί `int[]`, επιστρέφει `int`
- Array allocation (`new int[expr]`): απαιτεί `int` μέγεθος, επιστρέφει `int[]`
- Object allocation (`new A()`): ελέγχει ότι η κλάση υπάρχει, επιστρέφει τον τύπο της

**Statements:**
- Assignment: ο τύπος δεξιά πρέπει να είναι compatible με τον τύπο αριστερά (υποστηρίζεται subtyping)
- Array assignment: ελέγχει `int[]`, `int` index, `int` value
- If/While: η condition πρέπει να είναι `boolean`
- Print: η expression πρέπει να είναι `int`

**Method Calls (MessageSend):**
- Το αντικείμενο πρέπει να είναι τύπου κλάσης (όχι primitive)
- Η μέθοδος πρέπει να υπάρχει στην κλάση ή σε κάποιον πρόγονο
- Οι τύποι των arguments πρέπει να ταιριάζουν με τις παραμέτρους (υποστηρίζεται subtyping)
- Υποστηρίζεται αναζήτηση μεθόδων σε ολόκληρη την αλυσίδα κληρονομικότητας (μέθοδος `GetAllMethods`)

**Variable Lookup:**
- Αναζήτηση μεταβλητών με σειρά: local vars → parameters → fields → parent fields
- Local variable shadows field με ίδιο όνομα

**Return Type:**
- Ο τύπος του return expression πρέπει να είναι compatible με τον δηλωμένο τύπο επιστροφής

**Override/Overload:**
- Override: μέθοδος σε subclass με ίδιο όνομα και ίδιους τύπους παραμέτρων πρέπει να έχει ίδιο return type
- Overload: μέθοδοι με ίδιο όνομα πρέπει να ξεχωρίζουν unambiguously — πρέπει να υπάρχει τουλάχιστον μία θέση παραμέτρου χωρίς σχέση subtype/supertype
- Οι έλεγχοι γίνονται τόσο μέσα στην ίδια κλάση όσο και μεταξύ child/parent κλάσεων

**Βοηθητικές μέθοδοι:**
- `LookupType(var)` — Αναζήτηση τύπου μεταβλητής (local → params → fields → parent fields)
- `LookupFather(class, father)` — Ελέγχει αν μια κλάση είναι subtype μιας άλλης
- `GetAllMethods(class, method)` — Συλλέγει όλες τις μεθόδους με ένα όνομα από ολόκληρη την αλυσίδα κληρονομικότητας
- `isCompatible(from, to)` — Ελέγχει αν δύο τύποι είναι compatible (ίδιοι ή subtype)
- `IsPrimitive(type)` — Ελέγχει αν ένας τύπος είναι primitive (`int`, `boolean`, `int[]`)
- `CheckOverride(method)` — Ελέγχει σωστό overriding μεθόδου
- `CheckOverload(method)` — Ελέγχει ότι δεν υπάρχει ambiguous overloading

**Μηχανισμός isVariable:**
Η μεταβλητή `isVariable` χρησιμοποιείται για να ξεχωρίζει πότε ένα Identifier αναπαριστά μεταβλητή (χρειάζεται lookup τύπου) και πότε αναπαριστά όνομα κλάσης ή μεθόδου (επιστρέφεται ως string). Ενεργοποιείται στη `visit(PrimaryExpression)` και απενεργοποιείται σε σημεία όπως `AllocationExpression` και `MethodDeclaration` όπου τα Identifiers δεν είναι μεταβλητές.

### 3. OffsetCalculator — Υπολογισμός Offsets

Υπολογίζει και τυπώνει τα offsets για τα fields και τις methods κάθε κλάσης (εξαιρείται η MainClass).

**Μεγέθη τύπων:**
- `int` → 4 bytes
- `boolean` → 1 byte
- Pointers (class types, `int[]`) → 8 bytes
- Methods → 8 bytes (function pointer)

**Κανόνες:**
- Κλάσεις χωρίς extends: offsets ξεκινούν από 0
- Κλάσεις με extends: τα offsets συνεχίζουν από εκεί που τελείωσε ο parent
- Override methods δεν τυπώνονται (κρατούν το offset του parent)
- Overloaded methods τυπώνονται ξεχωριστά με suffix τύπων (π.χ. `foo`, `foo_int`, `foo_int_int`)

**Υλοποίηση:**
- `HashMap<String, Pair> class_offsets` — Αποθηκεύει τα τελικά offsets κάθε κλάσης (field_offset, method_offset) για χρήση από τα παιδιά
- `HashMap<String, Set<String>> methodSignatures` — Αποθηκεύει τα method signatures ανά κλάση για ανίχνευση overrides. Τα signatures κληρονομούνται από τον parent, και αν ένα νέο method signature υπάρχει ήδη στο set, αναγνωρίζεται ως override και παραλείπεται

---

## Σημειώσεις

- Η MainClass αντιμετωπίζεται ειδικά: δημιουργείται ένα MethodInfo για τη `main` ώστε να υποστηρίζεται semantic checking στο σώμα της, αλλά δεν εμφανίζεται στα offsets.
- Overloaded methods με ίδιο όνομα ομαδοποιούνται κάτω από το ίδιο key στο LinkedHashMap, οπότε η σειρά εκτύπωσης μπορεί να διαφέρει ελαφρώς από τη σειρά δήλωσης (αποδεκτό σύμφωνα με τους διδάσκοντες).
- Τα generated αρχεία (JTB + JavaCC output) βρίσκονται στον φάκελο `generated/` και παράγονται αυτόματα με `make`.
