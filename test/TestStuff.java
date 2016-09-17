import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestStuff {
    Character[] charactersOfAlphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'N', 'O', 'P', 'Q', 'R', 'S'};
    Character[] characterOfAlphabetAndSpecialCharacters = {'A', 'B', 'C', 'D', 'E', 'F', 'N', 'O', 'P', 'Q', 'R', 'S', ' ', '?' };

    @org.junit.Test
    public void shouldBeTheSameAfterEncodingTwice() throws Exception {

        for( int i = 0; i < 100; i++){
            String aRandomString = createARandomString(charactersOfAlphabet);
            String encode = CesarCipher.encode(CesarCipher.encode(aRandomString));

            assertThat(encode, equalTo( aRandomString ));
        }
    }

    @org.junit.Test
    public void shouldBeTheSameAfterEncodingTwiceWithSpecialChars() throws Exception {

        for( int i = 0; i < 100; i++){
            String aRandomString = createARandomString(characterOfAlphabetAndSpecialCharacters);
            String encode = CesarCipher.encode(CesarCipher.encode(aRandomString));

            assertThat(encode, equalTo( aRandomString ));
        }
    }

    @org.junit.Test
    public void shouldNotHaveLowercaseCharactersAfterEncoding() throws Exception {

        for( int i = 0; i < 100; i++){
            String aRandomString = createARandomString(charactersOfAlphabet);
            String encode = CesarCipher.encode(aRandomString);

            for(int j = 0; j < encode.length(); j++) {
                assertThat(encode.charAt(j) < (int) 'a', equalTo(true));
            }
        }
    }

    @org.junit.Test
    public void aStringWithRandomAlphabetShouldNotBeEncodedAsSimilarlyAsStringWithDefaultAlphabet() throws Exception {
        List<Character> aRandomAlphabetToEncodeWith = randomizeTheEncryptionAlphabet();

        int failedCount = 0;
        int totalRounds = 100000;
        for(int i = 0; i < totalRounds; i++){
            String aRandomString = createARandomString(charactersOfAlphabet);
            String encodeWithDefaultAlphabet = CesarCipher.encode(aRandomString);
            String encodeWithRandomAlphabet = CesarCipher.encode(aRandomString, aRandomAlphabetToEncodeWith);

            failedCount = encodeWithDefaultAlphabet.equals( encodeWithRandomAlphabet ) ?failedCount+1  : failedCount;

        }
        assertThat("Rounds: " + totalRounds + ", total of " + failedCount + " error occurred", failedCount < totalRounds / 100 , equalTo(true) );
    }

    @org.junit.Test
    public void shouldBeAbleToRandomizeTheAlphabetThatIsUsed() throws Exception {
        List<Character> characters = Arrays.asList(charactersOfAlphabet);
        Collections.shuffle( characters );

        for( int i = 0; i < 100; i++){
            String plaintext = createARandomString(characterOfAlphabetAndSpecialCharacters);
            String encode = CesarCipher.encode(plaintext, characters);
            String ciphertext = CesarCipher.encode(encode, characters);

            assertThat("alphabet: " + Joiner.on(',').join( characters ) + "\n" + "plaintext:  " + plaintext + " - \n" + "ciphertext: " + encode, ciphertext, equalTo( plaintext ));

        }
    }

    private List<Character> randomizeTheEncryptionAlphabet() {
        int indexOfElementToBeRemovedFromAlphabet = charactersOfAlphabet.length - 1;
        List<Character> randomAlphabet = Lists.newArrayList(charactersOfAlphabet) ;
        List<List<Character>> partition = Lists.partition(randomAlphabet, indexOfElementToBeRemovedFromAlphabet);
        Character character = partition.get(0).remove(partition.get(0).size() - 1);
        List<Character> collect = partition.stream().flatMap(List::stream).collect(Collectors.toList());
        Collections.shuffle( collect );
        collect.add( character );
        return collect;
    }

    @Test
    public void testUppercaseAMapsToUppercaseN() throws Exception {
        String result = CesarCipher.encode("A");

        assertThat(result, equalTo("N"));
    }


    @Test
    public void testWithSpecialChars() throws Exception {

        String result = CesarCipher.encode("A!");

        assertThat(result, equalTo("N!"));


    }

    private String createARandomString(Character[] characters) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        int randomStringLength = random.nextInt(100) + 1;
        for( int i = 0; i < randomStringLength; i++){
            int index = random.nextInt(characters.length);
            buffer.append(characters[index] );
        }
        return  buffer.toString();
    }

    private static class CesarCipher {

        private static List<Character> alphabetOfCipherCharacters = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'N', 'O', 'P', 'Q', 'R', 'S');

        public static String encode(String plaintext){
            return encodeWithAlphabet(plaintext, alphabetOfCipherCharacters);
        }

        public static String encode(String plaintext, List<Character> alphabetListToEncrypt) {
            return encodeWithAlphabet(plaintext, alphabetListToEncrypt);
        }

        private static String encodeWithAlphabet(String plaintext, List<Character> alphabetOfCipherCharacters) {
            String ciphertext = "";
            for(int i = 0; i < plaintext.length(); i++) {
                char plaintextCharacter = plaintext.charAt(i);
                ciphertext += (encodeCharacter(plaintextCharacter, alphabetOfCipherCharacters));
            }
            return ciphertext.toUpperCase();
        }

        private static Character encodeCharacter(char plaintextCharacter, List<Character> alphabetOfCharactersToBeCiphered) {
            if(isCharacterInTheAlphabetOfCharsToBeChanged(plaintextCharacter, alphabetOfCharactersToBeCiphered)){
                return encrypt(plaintextCharacter, alphabetOfCharactersToBeCiphered);
            } else {
                return plaintextCharacter;
            }
        }

        private static boolean isCharacterInTheAlphabetOfCharsToBeChanged(char plaintextCharacter, List<Character> characters) {
            return characters.contains(plaintextCharacter);
        }

        private static Character encrypt(char plaintextCharacter, List<Character> characters) {
            int sizeOfTheAlphabet = characters.size();
            int encryptionOffset = sizeOfTheAlphabet / 2;
            int offsetOfPlaintextCharacter = characters.indexOf(plaintextCharacter);
            return characters.get((offsetOfPlaintextCharacter + encryptionOffset) % sizeOfTheAlphabet);
        }
    }
}
