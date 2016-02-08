import java.io.*;
import java.util.*;

/**
 * Provides a factory method for creating word search games. 
 */
public class WordSearchGameEngine implements WordSearchGameInterface {


   TreeSet<String> WordBank;

   // TreeSet WordTree;
   String BoardLetters[][] =  {{"E", "E", "C", "A"},
                               {"A", "L", "E", "P"},
                               {"H", "N", "B", "O"},
                               {"Q", "T", "T", "Y"}};
   
   // = new TreeSet<String>();
   TreeSet<String> allWords;

  /**
	* Loads the lexicon into a data structure for later use. 
	* 
	* @param fileName A string containing the name of the file to be opened.
	* @throws IllegalArgumentException if fileName is null.
	* @throws IllegalArgumentException if fileName cannot be opened.
	*/
   public void loadLexicon(String fileName){
   
      if(fileName == null) {
         throw new IllegalArgumentException();
      }
      
      File Words = new File(fileName);
      WordBank = new TreeSet<String>();
       
      if(!Words.exists())throw new IllegalArgumentException();
      
      else {
         Scanner in;
         try{
            in = new Scanner(Words);
         }
         catch(Exception ex){throw new IllegalArgumentException();}
         
         while (in.hasNext()) {
            String line = in.next();
            //int breakingPoint = line.indexOf(" ");
            //if (breakingPoint > -1) {
            //line = text.substring(0, J - 1);
            //}
            Scanner s = new Scanner(line);
            
            WordBank.add(s.next().toUpperCase());
            
         }
         // WordTree = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
         // WordTree.addAll(WordBank);
      }
   
   }
   
  /**
	* Stores the incoming array of Strings in a data structure that will
	*      make it convenient to find words.
	* 
	* @param letterArray This array of length N^2 stores the contents of the
   *      game board in row-major order. Thus, index 0 stores the contents
   *      of board position (0,0) and index length-1 stores the contents
   *      of board position (N-1,N-1). Note that the board must be square
   *      and that the strings inside may be longer than one character. 
   *
	* @throws IllegalArgumentException if letterArray is null, or is -
	*      not square.-
	*/
   public void setBoard(String[] letterArray) {
      if(letterArray == null || Math.sqrt(letterArray.length)%1!=0.0)throw new IllegalArgumentException();
      
      int width = (int)Math.sqrt(letterArray.length);
      
      BoardLetters = new String[width][width];
      
      for (int i = 0; i < width; i++) {
         for (int j = 0; j < width; j++){
            BoardLetters[i][j] = letterArray[i * width + j].toUpperCase();//.charAt(j);
         }
      }
   }
   
   private int boardWidth()
   {
      return BoardLetters[0].length;
   }
   
  /**
   * Creates a String representation of the board, suitable for
   * printing to standard out. Note that this method can always be
   * called since implementing classes should have a default board.
   */
   public String getBoard(){
   
      String board = "";
   
      for (int i = 0; i < BoardLetters.length; i++) {
         for (int j = 0; j < BoardLetters.length ; j++){
            board = board + BoardLetters[i][j];
         }
         board = board + "\n";
      }
      return board;
   }

  /**
	* Retrieves all valid words on the game board, according to the
   * stated game rules. 
   * 
	* @param minimumWordLength The minimum allowed length (i.e., number-
	*	of characters) for any word found on the board.
   *
	* @return java.util.SortedSet which contains all the words of minimum
   *  length found on the game board and in the lexicon. 
   * 
   * If no words can be found, return null.
   *
	* @throws IllegalArgumentException if minimumWordLength < 1     
   * @throws IllegalStateException if loadLexicon has not been called.- 
	*/
   public SortedSet<String> getAllValidWords(int minimumWordLength) {
   	
   	//exceptions
      if(minimumWordLength < 1) {throw new IllegalArgumentException();} 
      if(WordBank == null) {throw new IllegalStateException();}
   	
   	//for making sure we don't use the same
   	//letter on the board twice in one word
      ArrayList<Integer> path = new ArrayList<Integer>(); 
      allWords = new TreeSet<String>();
      
   	//loop through the board
      for(int i = 0; i < BoardLetters.length; i++) {
         for(int j = 0; j < BoardLetters.length; j++) {
         	
         	//do we even need to bother with this letter?
         	// (""+char is a roundabout ways of casting char to string) 
            if(isValidPrefix(BoardLetters[i][j])) {
            
            	//add one letter
               path.add(i * BoardLetters.length + j);
            	
            	//one letter words
            	//checks length, if we already found this word somewhere else, and if it is a word
               if(minimumWordLength == 1 && !allWords.contains(""+BoardLetters[i][j]) && isValidWord(""+BoardLetters[i][j])) {
                  allWords.add(""+BoardLetters[i][j]);
               }
            	
            	//go deeper
               getAllValidWordsRecusive(minimumWordLength, path, ""+BoardLetters[i][j]);
            	
            	//reset and try a different first letter
               path.remove(0);
            	
            }
         }
      }
   	
   	//return all the words
      return allWords;
   }


   public void getAllValidWordsRecusive(int minimumWordLength, List<Integer> path, String tempWord) {
      int row = path.get(path.size() - 1)/BoardLetters.length;
      int col = path.get(path.size() - 1)%BoardLetters.length;
   	
   	// 3x3 loop
      for(int i = row - 1; i < row + 2; i++) {
         for(int j = col - 1; j < col + 2; j++) {
            if(i < 0 || j < 0 || i >= boardWidth() || j >= boardWidth())
               continue;
               
         	//do we even need to bother with this letter?
            if(!path.contains(i * boardWidth() + j) && isValidPrefix(tempWord + BoardLetters[i][j])) {
            	
            	//add letter
               tempWord += BoardLetters[i][j];
               path.add(i * BoardLetters.length + j);
            	
            	//add word
            	//checks length, if we already found this word somewhere else, and if it is a word
               if(tempWord.length() >= minimumWordLength && allWords.contains(tempWord) && isValidWord(tempWord)) {
                  allWords.add(tempWord);
               }
            	
            	//go deeper
               getAllValidWordsRecusive(minimumWordLength, path, tempWord);
            	
            	//remove letter to check a different one
               path.remove(path.size() - 1);
               tempWord = tempWord.substring(0,tempWord.length() - (tempWord.length() >= 2 ? 2 : 1)); //remove last character
            
            }
         }
      }
   	//no return neccessary because allWords is a global variable
   }
  /**
   * Computes the cummulative score for the scorable words in the given set.
   * To be scorable, a word must 
   * (1) have at least the minimum number of characters,
   * (2) be in the lexicon, and 
   * (3) be on the board. Each scorable word is
   * awarded one point for the minimum number of characters, and one point for 
   * each character beyond the minimum number.
   *
   * @param words The set of words that are to be scored.-
   * @param minimumWordLength The minimum number of characters required per word-
   *
   * @return the cummulative score of all scorable words in the set
   *
	* @throws IllegalArgumentException if minimumWordLength < 1 XXXT typed in - 
   * @throws IllegalStateException if loadLexicon has not been called. - 
   */  
   public int getScoreForWords(SortedSet<String> words, int minimumWordLength){
      if(minimumWordLength < 1)throw new IllegalArgumentException();
      if(WordBank == null)throw new IllegalStateException();
   
      Iterator<String> wordFinder = words.iterator();
      int score = 0;
   
      while (wordFinder.hasNext()) {
         String next = wordFinder.next();
       
         if(next.length() > minimumWordLength){
          
            score = next.length() - (minimumWordLength - 1) + score;
            
         }
         
              
      }
      return score;
      
   }
   

  /**
	* Determines if the given word is in the lexicon.
	* 
	* @param wordToCheck The word to validate
	* @return true if wordToCheck appears in lexicon, false otherwise.
   *
	* @throws IllegalArgumentException if wordToCheck is null. - 
   * @throws IllegalStateException if loadLexicon has not been called. -
	*/
   public boolean isValidWord(String wordToCheck){
   
      if(wordToCheck == null)throw new IllegalArgumentException();
      if(WordBank == null)throw new IllegalStateException();
      
      return WordBank.contains(wordToCheck.toUpperCase());
      
   }
   
  /**
	* Determines if there is at least one word in the lexicon with the 
	* given prefix.
	* 
	* @param prefixToCheck The prefix to validate
	* @return true if prefixToCheck appears in lexicon, false otherwise.
   *
	* @throws IllegalArgumentException if prefixToCheck is null. -- 
	* @throws IllegalStateException if loadLexicon has not been called. --
   */
   public boolean isValidPrefix(String prefixToCheck){
   	
      if(prefixToCheck == null)throw new IllegalArgumentException();
      if(WordBank ==null)throw new IllegalStateException();
      
      prefixToCheck = prefixToCheck.toUpperCase();
      return WordBank.subSet(prefixToCheck, prefixToCheck + Character.MAX_VALUE).size() > 0;
      
   }
      
  /**
	* Determines if the given word is in on the game board. If so, 
	*	it returns the path that makes up the word.
	* 
	* @param wordToCheck The word to validate -
	* @return java.util.List containing java.lang.Integer objects with 
	*	the path that makes up the word on the game board. If word
	*	is not on the game board, return null. Positions on the board are
   *  numbered from zero top to bottom, left to right (i.e., in row-major
   *  order). Thus, on an NxN board, the upper left position is numbered 
   *  0 and the lower right position is numbered N^2 - 1.
   *
   * @throws IllegalArgumentException if wordToCheck is null. --
   * @throws IllegalStateException if loadLexicon has not been called. --
	*/
   public List<Integer> isOnBoard(String wordToCheck){
      if(wordToCheck == null)throw new IllegalArgumentException();
      if(WordBank == null)throw new IllegalStateException();
      wordToCheck = wordToCheck.toUpperCase();
      List<Integer> letterList = new ArrayList<Integer>();
    
         
      for (int i = 0; i < BoardLetters.length ; i++) {
         for (int j = 0; j < BoardLetters.length ; j++){
            if (wordToCheck.startsWith(BoardLetters[i][j])){
               letterList.add(i + (j * BoardLetters.length));
            
               letterList = isOnBoard2(wordToCheck, letterList);
              
               if (letterList.size() == wordToCheck.length()){
                  return letterList;
               }
               letterList.remove(0);
               
            }     
          	
         }
      }
      return null;
   }
   
   public List<Integer> isOnBoard2(String wordToCheck, List<Integer> letterList){
               
      int row = (int)Math.floor(letterList.get(letterList.size()-1) / BoardLetters.length);
      int column = letterList.get(letterList.size()-1) % BoardLetters.length;
      int letterListLength = letterList.size();
               
      for (int k = row -1; k < row +2; k++) {
                  
         for (int m = column -1; m < column + 2; m++){
            
            if(k < 0 || m < 0 || k >= boardWidth() || k >= boardWidth()) 
               continue;
                 
            int candidate = k + (m * BoardLetters.length);
                     
            if (wordToCheck.startsWith(BoardLetters[k][m]) && !letterList.contains(candidate)){
                        
               letterList.add(candidate);
               if (wordToCheck.length() == letterList.size()){
                  return letterList;
                               
               }
               else if(wordToCheck.length() == isOnBoard2(wordToCheck, letterList).size()) {
                  return isOnBoard2(wordToCheck, letterList);
               }
               else {
                  letterList.remove(letterList.size() - 1);
               
               }
            }
         }
         
                
      }
    
      return letterList;
   }           
}
