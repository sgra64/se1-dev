## Branch Integration: *feat.730-json-data*

Branch *feat.730-json-data* enables a new feature to load data-objects (*Customer*, *Article*
and *Orders*) from *JSON files* replacing object creation in
[Application_F1.java](https://github.com/sgra64/se1.bestellsystem/blob/F12-Refactoring/src/application/Application_F1.java)
(before version).

The updated file (after integration of branch *feat.730-json-data*) will no longer
contain object creations:
[Application_F1.java](src/application/Application_F1.java)
(after version).

Instead, data will be obtained from JSON files in the new
[data](data) directory.

JSON file [data/customers.json](data/customers.json) contains *Customer* data:

```json
[
  {"id": 892474, "name": "Eric Meyer", "contacts": ["eric98@yahoo.com", "(030) 3945-642298"] },
  {"id": 643270, "name": "Bayer, Anne", "contacts": ["anne24@yahoo.de", "(030) 3481-23352", "fax: (030)23451356"] },
  {"id": 286516, "name": "Tim Schulz-Mueller", "contacts": ["tim2346@gmx.de"] },
  {"id": 412396, "name": "Nadine-Ulla Blumenfeld", "contacts": ["+49 152-92454"] },
  {"id": 456454, "name": "Khaled Saad Mohamed Abdelalim", "contacts": ["+49 1524-12948210"] },
  {"id": 651286, "name": "Lena Neumann", "contacts": ["lena228@gmail.com"] }
]
```

JSON file [data/articles.json](data/articles.json) contains *Article* data:

```json
[
  {"id": "SKU-458362", "description": "Tasse", "price": 299 },
  {"id": "SKU-693856", "description": "Becher", "price": 149 },
  {"id": "SKU-518957", "description": "Kanne", "price": 1999 },
  {"id": "SKU-638035", "description": "Teller", "price": 649 },
  {"id": "SKU-278530", "description": "Buch \"Java\"", "price": 4990, "tax": "reduced" },
  {"id": "SKU-425378", "description": "Buch \"OOP\"", "price": 7995, "tax": "reduced" },

  {"id": "SKU-300926", "description": "Pfanne", "price": 4999 },
  {"id": "SKU-663942", "description": "Fahrradhelm", "price": 16900 },
  {"id": "SKU-583978", "description": "Fahrradkarte", "price": 695, "tax": "reduced" }
]
```

JSON file [data/orders.json](data/orders.json) contains *Orders* data:

```json
[
  {"id": 8592356245, "customer_id": 892474, "items": [
    {"article_id": "SKU-638035", "units": 4 },
    {"article_id": "SKU-693856", "units": 8 },
    {"article_id": "SKU-425378", "units": 1 },
    {"article_id": "SKU-458362", "units": 4 }
  ]},

  {"id": 3563561357, "customer_id": 643270, "items": [
    {"article_id": "SKU-638035", "units": 2 },
    {"article_id": "SKU-458362", "units": 2 }
  ]},
  /* ... */
]
```

The branch contains a *code-drop* of new and modified files that enable the feature:

```
A       data/articles.json                          <-- new file (A: added)
A       data/articles_price_update.json             <-- new
A       data/customers.json                         <-- new
A       data/orders.json                            <-- new
M       resources/application.properties            <-- modified file (M: modified)
M       src/application/Application_F1.java         <-- modified
M       src/module-info.java                        <-- modified
A       src/system/impl/DataFactories.java          <-- new
M       src/system/impl/DataStoreImpl.java          <-- modified
M       src/system/impl/IoC_Impl.java               <-- modified
A       src/system/impl/JSONDataFactories.java      <-- new
A       src/system/impl/JSONRepositoryImpl.java     <-- new
```

Base of the *code-drop* is the state of the development after completing
[F12-Refactoring](https://github.com/sgra64/se1.bestellsystem/tree/F12-Refactoring)


&nbsp;

## Steps

Refer to `F12-Refactoring`
[setup](https://github.com/sgra64/se1.bestellsystem/tree/F12-Refactoring#setup)
for completing some of the steps below (e.g. for adding remotes, fetching
branches or setting up local tracking branches).

1. Make sure, code from completed assignment `F12-Refactoring` compiles
    and JUnit tests pass.

1. Create a new integration branch: `int.feat.730-json-data`*` at the last
    commit of the completed (working) `F12-Refactoring`.
    The integration branch will contain this code.

1. Fetch branch `feat.730-json-data` from a new downstream repository where
    another team develops its features: `https://github.com/sgra64/se1-dev.git`.

1. Create a local tracking branch `feat.730-json-data` for the fetched remote
    branch.

    ```sh
    git branch -u se1-dev/feat.730-json-data feat.730-json-data
    ```

1. Merge the feature from the tracking branch into the integration branch
    (containing the previous state of `F12-Refactoring`):

    ```sh
    # switch to integration branch
    git switch int.feat.730-json-data

    # merge branch feat.730-json-data into integration branch
    git merge --allow-unrelated-histories feat.730-json-data
    ```

    - The Merge will cause *merge conflicts*.

    - Open an affected file in *VSCode* and navigate to the conflict
        (*VSCode* highlights conflicts).
        
    - Resolve the conflict (usually by accepting *Incoming Changes*)

    - The merge remains *open* while all conflicts are being resolved.

    - The Merge is *finalized* and *closed* by either:

        - aborting the merge and removing all changes: `merge --abort` or by

        - committing the merge: `git add .`
            and `git commit -m "merge commit feat.730-json-data"`

        Commit the merge only when the program works and all JUnit Tests pass!

1. After integration has completed on the integration branch,
    merge it back to your main development branch (where the integration
    branch was created).

    This merge will be a `fast-forward` merge since no other changes have
    occured on the main branch. No conflicts will occur.

    The main development branch now has the feature integrated and
    development can continue.

1. Draw branches and commits for the integration.

1. Push commits with the integration to your remote main development branch.


&nbsp;

## Validation

Switch to your main development branch, build and run the program:

```sh
mk clean compile compile-tests run-tests
```
```
[       121 tests successful      ]     <-- 121 tests passed
[         0 tests failed          ]
```

Run the program:

```sh
mk run
```
```
Bestellungen:
+----------+--------------------------------------------+----------------------+
|Bestell-ID| Bestellungen             MwSt*        Preis|     MwSt       Gesamt|
+----------+--------------------------------------------+----------------------+
|6135735635| Nadine-Ulla's Bestel                       |                      |
|          |  - 12 Teller, 12x 6.    12.43     77.88 EUR|                      |
|          |  - 1 Buch "Java"         3.26*    49.90 EUR|                      |
|          |  - 1 Buch "OOP"          5.23*    79.95 EUR|    20.92   207.73 EUR|
+----------+--------------------------------------------+----------------------+

...

+----------+--------------------------------------------+----------------------+
                                                 Gesamt:|    76.78   642.70 EUR|
                                                        +======================+
```


&nbsp;

### Update Pricing

Update pricing by loading new JSON file
[data/articles_price_update.json](data/articles_price_update.json)
into the program.

1. Edit configuration file
    [resources/application.properties](resources/application.properties).

1. Since no code needed to be changed for the price update, the
    change is effective after restart. No re-compilation is needed.

    Re-run the application:

    ```sh
    mk run
    ```

Output with the price update:

```
Artikel:
+----------+----------------------------+---------------+----------------------+
|Artikel-ID| Beschreibung               |      Preis CUR|  Mehrwertsteuersatz  |
+----------+----------------------------+---------------+----------------------+
|SKU-693856| Becher                     |       2.49 EUR|  19.0% GER_VAT       |  <-- was   1.49
|SKU-638035| Teller                     |       8.49 EUR|  19.0% GER_VAT       |  <-- was   6.49
|SKU-425378| Buch "OOP"                 |      99.95 EUR|   7.0% GER_VAT_REDU  |  <-- was  79.95
|SKU-300926| Pfanne                     |      69.99 EUR|  19.0% GER_VAT       |  <-- was  49.99
|SKU-458362| Tasse                      |       4.99 EUR|  19.0% GER_VAT       |  <-- was   2.99
|SKU-278530| Buch "Java"                |      69.90 EUR|   7.0% GER_VAT_REDU  |  <-- was  49.90
|SKU-518957| Kanne                      |      29.99 EUR|  19.0% GER_VAT       |  <-- was  19.99
|SKU-663942| Fahrradhelm                |     199.00 EUR|  19.0% GER_VAT       |  <-- was 169.00
|SKU-583978| Fahrradkarte               |       8.95 EUR|   7.0% GER_VAT_REDU  |  <-- was   6.95
+----------+----------------------------+---------------+----------------------+

Bestellungen:
+----------+--------------------------------------------+----------------------+
|Bestell-ID| Bestellungen             MwSt*        Preis|     MwSt       Gesamt|
+----------+--------------------------------------------+----------------------+
|6135735635| Nadine-Ulla's Bestel                       |                      |
|          |  - 12 Teller, 12x 8.    16.27    101.88 EUR|                      |
|          |  - 1 Buch "Java"         4.57*    69.90 EUR|                      |
|          |  - 1 Buch "OOP"          6.54*    99.95 EUR|    27.38   271.73 EUR|
+----------+--------------------------------------------+----------------------+
|6173043537| Lena's Bestellung:                         |                      |
|          |  - 1 Buch "Java"         4.57*    69.90 EUR|                      |
|          |  - 1 Fahrradkarte        0.59*     8.95 EUR|     5.16    78.85 EUR|
+----------+--------------------------------------------+----------------------+
|8592356245| Eric's Bestellung:                         |                      |
|          |  - 4 Teller, 4x 8.49     5.42     33.96 EUR|                      |
|          |  - 8 Becher, 8x 2.49     3.18     19.92 EUR|                      |
|          |  - 1 Buch "OOP"          6.54*    99.95 EUR|                      |
|          |  - 4 Tasse, 4x 4.99      3.19     19.96 EUR|    18.33   173.79 EUR|
+----------+--------------------------------------------+----------------------+
|5234968294| Eric's Bestellung:                         |                      |
|          |  - 1 Kanne               4.79     29.99 EUR|     4.79    29.99 EUR|
+----------+--------------------------------------------+----------------------+
|4450305661| Eric's Bestellung:                         |                      |
|          |  - 3 Tasse, 3x 4.99      2.39     14.97 EUR|                      |
|          |  - 3 Becher, 3x 2.49     1.19      7.47 EUR|                      |
|          |  - 1 Kanne               4.79     29.99 EUR|     8.37    52.43 EUR|
+----------+--------------------------------------------+----------------------+
|7372561535| Eric's Bestellung:                         |                      |
|          |  - 1 Fahrradhelm        31.77    199.00 EUR|                      |
|          |  - 1 Fahrradkarte        0.59*     8.95 EUR|    32.36   207.95 EUR|
+----------+--------------------------------------------+----------------------+
|3563561357| Anne's Bestellung:                         |                      |
|          |  - 2 Teller, 2x 8.49     2.71     16.98 EUR|                      |
|          |  - 2 Tasse, 2x 4.99      1.59      9.98 EUR|     4.30    26.96 EUR|
+----------+--------------------------------------------+----------------------+
                                                 Gesamt:|   100.69   841.70 EUR|  <-- updated
                                                        +======================+
```


&nbsp;

### Answer Questions

1. What does `git remote add upstream git@...` do?

    - What is *"upstream"* in this expression?

1. What does `git fetch` do?

1. What is a *remote branch*?

1. What is a *tracking branch*?

1. What is a *merge conflict*?

    - How is a *merge conflict* marked in files?

    - How is a *merge conflict* resolved?

    - What does *"Accept Incoming"* compared to *"Accept Current"* mean?

    - Can *all* merge conflicts be resolved by selecting these options?

1. What does it mean that a *"merge is open"*?

1. How is an *open merge* closed (there are two alternatives)?

1. What is the result of a regular *merge*?

1. What is a *fast-forward (ff)* merge?

1. What happens with the merged branch after the merge?

1. Can a merged branch be removed?

1. What does `git gc` do?

    - Why should it be performed?

    - Which *"objects"* are removed?


<!-- 
```sh
mkdir f12json
cd f12json
git init
touch .gitignore
git add .
git commit -m "initial .gitignore"

git remote add se1-dev https://github.com/sgra64/se1-dev.git

# switch to main development branch, here: "main"
git switch main
git pull se1-dev f12-base --squash --allow-unrelated-histories --strategy-option theirs
git commit -m "pull/merge from se1-dev/f12-base"

source .env/setenv.sh
mk clean compile compile-tests run-tests
mk run


# fetch remote branch
git fetch se1-dev feat.730-json-data:feat.730-json-data

# create local tracking branch
git branch -u se1-dev/feat.730-json-data feat.730-json-data

# create/switch to new branch for the integration
git checkout -b int.feat.730-json-data

# merge the feature into t
git merge --allow-unrelated-histories feat.730-json-data

Auto-merging README.md
CONFLICT (add/add): Merge conflict in README.md
Auto-merging resources/application.properties
CONFLICT (add/add): Merge conflict in resources/application.properties
Auto-merging src/application/Application_F1.java
CONFLICT (add/add): Merge conflict in src/application/Application_F1.java
Auto-merging src/module-info.java
CONFLICT (add/add): Merge conflict in src/module-info.java
Auto-merging src/system/impl/DataStoreImpl.java
CONFLICT (add/add): Merge conflict in src/system/impl/DataStoreImpl.java
Auto-merging src/system/impl/IoC_Impl.java
CONFLICT (add/add): Merge conflict in src/system/impl/IoC_Impl.java
Automatic merge failed; fix conflicts and then commit the result.

git diff --check

mk clean compile compile-tests run-tests
mk run

git add .
git commit -m "merge commit feat.730-json-data"

# switch to main development branch, here: "main"
git switch main

# fast-forward merge
git merge int.feat.730-json-data

# delete branches
git branch -d int.feat.730-json-data
git branch -d feat.730-json-data
git branch -d -r se1-dev/feat.730-json-data

git count-objects
350 objects, 411 kilobytes

git gc
``` -->
