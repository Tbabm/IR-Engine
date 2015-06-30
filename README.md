# IR-Engine
Homework for IR

## Introduction

IR = Information Retrieval

This is a small information retreival engine.

The data we used in this search engine for test was from Reuters.

## what we implement?

1. use Posting list, B+ tree to construct and store information and index.

2. use vector-space-search, bool-search techinique to search information.

3. In addition, we also implement some additional functions, such as spell correction (effective), index compression (reduce 50% storage).

## The structure

1. src folder contains code files, in which the Index folder contains files of Index construction system, the Search folder contains files used for search module, and the run folder contains the only single file which includes main function.

2. material folder contains the data. We use Raw_Stemmed_Dic (used for stem reduction) and vb_ri.index (compressed index) now.

