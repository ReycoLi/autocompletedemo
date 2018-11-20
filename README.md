# Autocomplete-App
<img src="https://github.com/ReycoLi/autocompletepoc_1/blob/master/app_preview0.png" height="300" width="300">

## Introduction
 This is a Java web-app demo. It takes a list of (name, score) pairs as input. (Basically, the input should be a binary file of List<Pair>). Then it could recommend serveral names with highest score based on the input List.


## Design
### Core Objects:
| Core Object | Description |
| :--- | :--- |
| DictionaryTrie | Singleton object, store the information of all pairs and serve for searching. |
| TrieNode | The unit of a Trie. One attribute is its child trie nodes, the other is List of pairs |
| Pair | Store the information of a single (name, score) pair. |
| SerializeUtil | The util for persisting objects to file. |
| PairGenerator | The util for randomly generating centain number of pairs for test. |
| PropertyUtil | The util for helping spring component obtain the configuration information in application.properties. |

### Initialize dictionary trie
If you have your own dictionary(a binary file of a list of pairs), place it in project folder and edit the input_file_path_name attribute in application.properties. The app will deserialize this input file to a list of pair.  
If you only use this app for demo or testing, the app will automaticlly generate a list of pairs as input. you can customize the rules of how to generate pairs in application.properties (details in the following Customized section).     
Then, to build the dictionary trie by inserting all this pairs to root TrieNode.

### Store information of input pairs in DictionaryTrie:    
1. To store the pair without underscore like pair("abc", 100)  
    It should start from the root TrieNode，follow the path a->b->c and add this pair("abc", 100) to the list in last Trienode c.

2. To store the pair with underscore like pair("abc_def", 200)  
    First, it should follow the path a->b->c->_->d->e->f and add this pair("abc_def", 200) to the list in last Trienode f.    
    After that, it still needs to follow the path d->e->f and add this pair("abc_def", 200) to the list in last Trienode f. 
    
### Serialize and Deserialize
When initialization of dictionary trie finish, the app will persisted the dictionary trie to file automaticlly.  
So when restart this application, it will initialize the dictionary trie by deserializing the file instead of rebuilding the trie from the input list of pairs. All the processes are automatic.
 
### Serve:
1. Serve for searching 
    If input prefix is 'sc' (like the front demo image), it will traverse all the child nodes of 'sc' in Trie tree and find the first K     names with the highest score by maintaining a priority queue.
    
2. Cache  
    After finish a search, it will save the result to the HashMap in Dictionary Trie. When a new search come, it will check the cache first.


## Customize
You can customize the following parameter in application.properties file (src/main/resources/application.properties) for initialization.

1. input_file_path_name  
You could use yourself List of pairs as dictionary. Just replace this attribute by your file name with the path where you save this file, ex"src/main/resources/user_customized_file.txt". 
```shell
Note: this should be a binary file of List<Pair>. You can serialize your List<Pair> to binary file using Java ObjectInputStream and Java ObjectOutputStream.
```

2. trie_file_path_name  
The file name and path where you want to save the serialized dictionary trie. ex"src/main/resources/Dictionay_Trie.txt"

3. search_amount  
The amount of names you want to recommend on UI.

    if you only use this app for demo and don't have you own input file, this app can help you generate several ramdom pairs as input for you. And you can customize the following properties.

4. pair_amount  
The amount of random pairs you want to generate.

5. maxString_length  
The max length of random name you want to generate.

6. max_score  
The max score of random pairs.

## Environment Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

INSTALL:

```shell
$ git clone https://github.com/ReycoLi/autocompletepoc_1.git
```

RUN SERVER:
```shell
$ cd autocompletepoc_1
$ mvn clean install spring-boot:run
```

RUN UI:
```bash
Open the following page: http://localhost:8080
```
