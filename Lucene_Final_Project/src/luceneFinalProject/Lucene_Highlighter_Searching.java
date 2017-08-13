/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luceneFinalProject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;

/**
 *
 * @author PeaceFull
 */
public class Lucene_Highlighter_Searching {
 
     public void highlighterSearch(Directory indexDirectory,StandardAnalyzer analyzer)throws IOException, ParseException, InvalidTokenOffsetsException{
          QueryParser qp = new QueryParser("contents", analyzer);

        //Create the query
        String queryString = "Java";
        Query query = qp.parse(queryString);

        // 3. searching
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(indexDirectory);
        IndexSearcher searcher = new IndexSearcher(reader);

        TopDocs hits = searcher.search(query, hitsPerPage);
 
        QueryScorer scorer = new QueryScorer(query);
        //used to markup highlighted terms found in the best sections of a text
        Formatter formatter = new SimpleHTMLFormatter();
        Highlighter highlighter = new Highlighter(formatter, scorer);
        int fragmetSize = 10;
        //It breaks text up into same-size texts but does not split up spans
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer,fragmetSize);

        highlighter.setTextFragmenter(fragmenter);

        highlighter.setTextFragmenter(fragmenter);

       
        for (int i = 0; i < hits.scoreDocs.length; i++) {
            int docid = hits.scoreDocs[i].doc;
            Document d = searcher.doc(docid);

            String path = d.get("filePath");

            //Printing - to which document result belongs
            System.out.println("Path " + " : " + path);
            String text = d.get("contents");

            //Create token stream
            TokenStream stream = TokenSources.getAnyTokenStream(reader, docid, "contents", analyzer);

            //Get highlighted text fragments
            String[] frags = highlighter.getBestFragments(stream, text, 10);
            for (String frag : frags) {
                System.out.println("=======================");
                System.out.println(frag);
            }

            //        System.out.println("Result"+(i + 1) + ". " + "\t" + d.get("fileName")+"\t"+d.get("filePath")+"\t"+printLines(d.get("filePath"),queryString));
        }
    }
    
    
    public int printLines(String filepath, String key) throws FileNotFoundException, IOException {
        int counter = 1;
        String line;

        // Read the file and display it line by line.
        BufferedReader file = new BufferedReader(new FileReader(filepath));
        while ((line = file.readLine()) != null) {
            if (line.contains(key)) {
                // System.out.println("\n"+counter + ": " + line);
                break;
              
            }
            counter++;
        }
        file.close();
        return counter;

    }
}
