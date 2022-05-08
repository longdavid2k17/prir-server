package com.asd.prirserver.hibernatesearch;

import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;

public class LuceneAnalysisConfigurer  implements org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer {
    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        context.analyzer( "name" ).custom()
                .tokenizer( "standard" )
                .tokenFilter( "lowercase" )
                .tokenFilter( "asciiFolding" );
    }
}
