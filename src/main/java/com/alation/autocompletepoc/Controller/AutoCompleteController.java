package com.alation.autocompletepoc.Controller;

import com.alation.autocompletepoc.Domain.PrefixCriteria;
import com.alation.autocompletepoc.util.Domain.DictionaryTrie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AutoCompleteController {

    @Autowired
    private DictionaryTrie dictionaryTrie;

    @PostMapping (value="/getTopTen")
    public List<String> searchByPrefix(@RequestBody PrefixCriteria prefixCriteria){
        String prefix = prefixCriteria.getPrefix();
        return dictionaryTrie.search(prefix);
    }
}
