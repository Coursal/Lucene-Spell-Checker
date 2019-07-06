package spell_checker;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.JaroWinklerDistance;
import org.apache.lucene.search.spell.LevenshteinDistance;
import org.apache.lucene.search.spell.LuceneLevenshteinDistance;
import org.apache.lucene.search.spell.NGramDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Spell_Checker
{
    public void check_txt_dictionary(String input_word) throws IOException
    {
        // Creating the index
        Directory directory = FSDirectory.open(Paths.get("Index"));
        PlainTextDictionary txt_dict = new PlainTextDictionary(Paths.get("eng_and_gr_dictionary.txt"));
        SpellChecker checker = new SpellChecker(directory);

        System.out.print("\nBuilding index from the .txt dictionary took... ");
        long start_time = System.currentTimeMillis();
            checker.indexDictionary(txt_dict, new IndexWriterConfig(new KeywordAnalyzer()), false);
            directory.close();
        long end_time = System.currentTimeMillis();
        System.out.println((end_time - start_time)/1000 + " seconds.\n");
        
        
        // Searching and presenting the suggested words by selecting a string distance
        checker.setStringDistance(new JaroWinklerDistance());
        //checker.setStringDistance(new LevenshteinDistance());
        //checker.setStringDistance(new LuceneLevenshteinDistance());
        //checker.setStringDistance(new NGramDistance()); 
        
        String[] suggestions = checker.suggestSimilar(input_word, 5);
        
        System.out.println("By '" + input_word + "' did you mean:");
        for(String suggestion : suggestions)
            System.out.println("\t" + suggestion);
    }
    
    public static void main(String[] args) throws IOException, Throwable
    {
        Scanner scan = new Scanner(System.in);
        Spell_Checker spell_checker = new Spell_Checker();
  
        System.out.print("\nType a word to spell check: ");
        String input_word = scan.next();

        spell_checker.check_txt_dictionary(input_word);
    }
}