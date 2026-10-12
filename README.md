# Course Recommendation System Using Advanced Data Structures and Algorithms

##  Project Overview

The **Course Recommendation System** is a Java-based application that recommends relevant courses to students based on their interests, skills, and career goals. It uses a text-based course corpus and advanced string-matching, dynamic programming, graph, approximation, and randomized algorithms to identify and rank suitable courses.

The project demonstrates how theoretical concepts from Data Structures and Algorithms (DSA) can be applied to a practical text-analytics and recommendation problem.

##  Objectives

- Recommend courses based on student interests and keywords.
- Implement efficient pattern-matching algorithms for text search.
- Support fuzzy matching to handle spelling mistakes in queries.
- Demonstrate graph-based matching and approximation algorithms.
- Explore randomized hashing for efficient word indexing.
- Analyze the time complexity and practical applications of advanced algorithms.

##  Technologies Used

- **Programming Language:** Java
- **Development Environment:** Any Java-compatible IDE or terminal
- **Data Storage:** Text files (`.txt`)
- **Core Concepts:** String Matching, Dynamic Programming, Graph Algorithms, Approximation Algorithms, Randomized Algorithms

##  Project Structure

```text
Course-Recommendation-System/
│
├── Main.java
├── CourseRecommender.java
├── KMP.java
├── ZAlgorithm.java
├── EditDistance.java
├── FlowMatcher.java
├── SkillCover.java
├── RandomizedCourseIndex.java
│
├── Corpus/
│   ├── INDEX.txt
│   ├── course1.txt
│   ├── course2.txt
│   └── ...
│
└── README.md
```

*Note: The structure above illustrates the main project components. Actual filenames and folder organization may vary.*

##  Modules and Algorithms Implemented

### CO1: Text Analytics and Course Recommendation Engine

The main recommendation engine accepts student queries, extracts keywords, reads course information from the `Corpus/` directory, calculates relevance scores, and displays ranked recommendations.

**Key operations:**
- Keyword extraction and preprocessing
- Reading and parsing course text files
- Calculating course relevance scores
- Sorting courses based on relevance
- Displaying recommended courses

### CO2: String Matching Algorithms

**Algorithms:** Knuth-Morris-Pratt (KMP) and Z-Algorithm

These algorithms efficiently search for keywords in course descriptions and related fields.

- **KMP:** Uses the Longest Prefix Suffix (LPS) array to avoid unnecessary character comparisons.
- **Z-Algorithm:** Uses a Z-array to identify occurrences of a pattern within a text.

Both algorithms achieve linear time complexity of **O(n + m)**, where `n` is the text length and `m` is the pattern length.

### CO3: Dynamic Programming — Edit Distance

**Algorithm:** Levenshtein Edit Distance (Wagner–Fischer)

This module enables fuzzy keyword matching by identifying words that are similar to the user's query, even when spelling mistakes occur.

The algorithm calculates the minimum number of insertions, deletions, and substitutions required to transform one string into another.

**Time Complexity:** O(n × m)

### CO4: Graph Algorithms — Bipartite Matching

**Algorithm:** Augmenting-path-based bipartite matching

This module demonstrates matching between student interests and suitable courses. It attempts to assign interests to courses while avoiding duplicate assignments and explores alternative assignments when conflicts occur.

The approach illustrates the relationship between bipartite matching and maximum-flow algorithms.

### CO5: Approximation Algorithms — Vertex Cover

**Algorithm:** 2-Approximation for Vertex Cover

This module demonstrates how approximation algorithms can solve computationally difficult optimization problems efficiently.

The algorithm selects endpoints of uncovered edges to construct a vertex cover. It provides a solution whose size is at most twice the size of an optimal minimum vertex cover.

### CO6: Randomized Algorithms — Course Indexing

**Algorithm:** Randomized polynomial hashing

This module demonstrates randomized hashing to build a word index for course-related text. Query words are hashed and checked against indexed entries.

The implementation also
