package com.zdm.androidtest.search;

import android.content.SearchRecentSuggestionsProvider;

/**
 * @author bill
 *	创建类SearchSuggestionSampleProvider.java用户查询数据保存的配置信息
 */
public class SearchSuggestionSampleProvider extends
		SearchRecentSuggestionsProvider {

	public final static String AUTHORITY="SuggestionProvider";  
    public final static int MODE=DATABASE_MODE_QUERIES;  
      
    public SearchSuggestionSampleProvider(){  
        super();  
        setupSuggestions(AUTHORITY, MODE);  
    }  

}
