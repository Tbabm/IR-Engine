# IR-Engine
Homework for IR

## Introduction

IR = Information Retrieval

This is a small information retreival engine.

The data we used in this search engine for test was from Reuters.

## what we implement?

1. use Postinglist, B+ tree to construct and store information and index

2. use vector-based search, bool-search techinique to search information

3. In addition, we also implement some additional function, such as spell correction, index compression.

## The structure

1. src folder containes code, in which the Index folder contains file in Index construction system, the Search folder contains files used for search function, and the run folder contains the only single file which includes main function.

2. material folder contains the data. We use Raw_Stemmed_Dic and vb_ri.index now.

