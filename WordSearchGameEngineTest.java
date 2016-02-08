import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class WordSearchGameEngineTest {

   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }

   /** A test that always fails. **/
   @Test public void oneByOne() {
   
      WordSearchGameEngine game = new WordSearchGameEngine();
      game.loadLexicon("wordfiles/words_medium.txt");
      game.setBoard(new String[]{"A"});
      game.isValidWord("A");
      game.isValidWord("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZzz");
      game.getAllValidWords(3);
      //Assert.assertEquals("oneByOne Fail.", ,);
   }
   
    /** A test that always fails. **/
   @Test public void TigerTest() {
   
      WordSearchGameEngine game = new WordSearchGameEngine();
      game.loadLexicon("wordfiles/words_medium.txt");
      game.setBoard(new String[]{"TIGER"});
      game.isValidWord("A");
      game.isValidWord("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZzz");
      SortedSet<String> res = game.getAllValidWords(3);
      Assert.assertTrue(res != null);
      
      Assert.assertTrue(res.size() == 1);
      //Assert.assertEquals("oneByOne Fail.", ,);
   }
   
   /** A test that always fails. **/
   @Test public void TwByTw() {
   
      WordSearchGameEngine game = new WordSearchGameEngine();
      game.loadLexicon("wordfiles/words_medium.txt");
      game.setBoard(new String[]{"C","A","X","T",});
      game.isValidWord("A");
      game.isValidWord("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZzz");
      game.isOnBoard("A");
      SortedSet<String> res = game.getAllValidWords(3);
      //Assert.assertTrue(res != null);
      
      //Assert.assertTrue(res.size() == 1);
      //Assert.assertEquals("oneByOne Fail.", ,);
   }
}
