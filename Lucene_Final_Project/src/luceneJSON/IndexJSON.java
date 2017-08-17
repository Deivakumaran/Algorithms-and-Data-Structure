/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luceneJSON;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import static junit.framework.Assert.assertEquals;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author PeaceFull
 */
public class IndexJSON {

    /**
     * @param args the command line arguments
     *
     */
    String jsonFilePath = "E:\\Algorithms and Data Structure\\Summer-2017\\Final_Project\\JSON_input";

    String indexPath = "E:\\Algorithms and Data Structure\\Summer-2017\\Final_Project\\JSON_Output";

    IndexWriter indexWriter = null;

    public IndexJSON() {
        //    this.indexPath = indexPath;
        //  this.jsonFilePath = jsonFilePath;
    }

    public void createIndex() throws IOException, ParseException {
        openIndex();
        File dir = new File(jsonFilePath);
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isFile()) {

                String filePath = file.getAbsolutePath();
                String fileName = file.getName();
                JSONArray jsonObjects = parseJSONFile(filePath);

                addDocuments(jsonObjects, filePath, fileName);
                //  BufferedReader inputStream = new BufferedReader(new FileReader(file));
                // addPlainTextDocuments(writer, inputStream, file.getPath(), file.getName());
            }
        }

        finish();
    }

    public JSONArray parseJSONFile(String filePath) throws IOException, ParseException {
        //Get the JSON file, in this case is in ~/resources/test.json
        /*  InputStream jsonFile = getClass().getResourceAsStream(jsonFilePath);

        Reader readerJson = new InputStreamReader(jsonFile);
        //Parse the json file using simple-json library
        Object fileObjects = JSONValue.parse(readerJson);
        JSONArray arrayObjects = (JSONArray) fileObjects;
        return arrayObjects;
        
         */

        JSONParser parser = new JSONParser();
        JSONArray arrayObjects = (JSONArray) parser.parse(new FileReader(filePath));
        for (Object o : arrayObjects) {
            JSONObject person = (JSONObject) o;
            String name = person.get("name").toString();
            System.out.println(name);

            Double city = (Double) person.get("lat");
            System.out.println(city);

        }
        return arrayObjects;

    }

    public boolean openIndex() {
        try {
            StandardAnalyzer analyzer = new StandardAnalyzer();
            //   String INDEX_DIRECTORY = "E:\\Algorithms and Data Structure\\Summer-2017\\Final_Project\\JSON_Output";
            Directory indexDirectory = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(indexDirectory, config);
            return true;
        } catch (Exception e) {
            System.err.println("Error opening the index. " + e.getMessage());
        }
        return false;

    }

    public void addDocuments(JSONArray jsonObjects, String filePath, String fileName) {

        for (JSONObject object : (List<JSONObject>) jsonObjects) {
            Document doc = new Document();
            doc.add(new StringField("fileName", fileName, Field.Store.YES));// use a string field for course_code because we don't want it tokenized
            doc.add(new StringField("filePath", filePath, Field.Store.YES));

            for (String field : (Set<String>) object.keySet()) {
                Class type = object.get(field).getClass();
                if (type.equals(String.class)) {
                    doc.add(new StringField(field, String.valueOf(object.get(field)), Field.Store.NO));
                } else if (type.equals(Long.class)) {

                    doc.add(new StringField(field, String.valueOf(object.get(field)), Field.Store.YES));
                } else if (type.equals(Double.class)) {

                    doc.add(new StringField(field, String.valueOf(object.get(field)), Field.Store.YES));
                } else if (type.equals(Boolean.class)) {
                    doc.add(new StringField(field, object.get(field).toString(), Field.Store.YES));
                }
            }
            try {
                indexWriter.addDocument(doc);
            } catch (IOException ex) {
                System.err.println("Error adding documents to the index. " + ex.getMessage());
            }
        }
    }

    public void finish() {
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException ex) {
            System.err.println("We had a problem closing the index: " + ex.getMessage());
        }
    }

    public static void main(String[] args) throws IOException, org.apache.lucene.queryparser.classic.ParseException {
        // TODO code application logic here

        String INDEX_PATH = "E:\\Algorithms and Data Structure\\Summer-2017\\Final_Project\\JSON_Output";
        //  String JSON_FILE_PATH = "E:\\Algorithms and Data Structure\\Summer-2017\\Final_Project\\JSON_File.json";

        try {
            IndexJSON lucene = new IndexJSON();
            lucene.createIndex();

            Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_PATH));
            IndexReader indexReader = DirectoryReader.open(indexDirectory);
            int numDocs = indexReader.numDocs();
            assertEquals(numDocs, 3);
            for (int i = 0; i < numDocs; i++) {
                Document document = indexReader.document(i);
                System.out.println("Indexed Document" + (i + 1) + " is :" + '\n');
                System.out.println("d=" + document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_PATH));
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        final IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
                new String[]{"name", "studyId"},
                new StandardAnalyzer());

        Query query = queryParser.parse("1");

        //   Term t = new Term("name", "id2");
        //   Query query = new TermQuery(t);
        int hitsPerPage = 10;

        TopDocs topDocs = indexSearcher.search(query, hitsPerPage);
        assertEquals(1, topDocs.totalHits);

        //  TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        // ScoreDoc[] hits = collector.topDocs().scoreDocs;
        System.out.println("Found " + topDocs.totalHits + " hits.");

        for (int i = 0; i < topDocs.scoreDocs.length; i++) {
            int docid = topDocs.scoreDocs[i].doc;
            Document d = indexSearcher.doc(docid);

            //Printing - to which document result belongs
            System.out.println("Path of the file" + " : " + d.get("filePath"));
            
            System.out.println("Name of the file" + " : " + d.get("fileName"));
            System.out.println('\n' + "The document found is :" + d);

        }

    }

}
