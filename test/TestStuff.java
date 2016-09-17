import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestStuff {
    Character[] charactersOfAlphabet = {'A', 'B', 'C', 'N', 'O', 'P'};
    Character[] characterOfAlphabetAndSpecialCharacters = {'A', 'B', 'C', 'N', 'O', 'P', ' ', '?' };

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
    public void shouldBeAbleToRandomizeTheAlphabetThatIsUsed() throws Exception {
        List<Character> characters = Arrays.asList(charactersOfAlphabet);
        Collections.shuffle( characters );

        for( int i = 0; i < 100; i++){
            String aRandomString = createARandomString(characterOfAlphabetAndSpecialCharacters);
            String encode = CesarCipher.encode(aRandomString, characters);

            for(int j = 0; j < encode.length(); j++) {
                assertThat(encode.charAt(j) < (int) 'a', equalTo(true));
            }
        }
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

        private static List<Character> alphabetOfCipherCharacters = Arrays.asList('A', 'B', 'C', 'N', 'O', 'P');

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

        private static Character encodeCharacter(char plaintextCharacter, List<Character> characters) {
            if(isCharacterInTheAlphabetOfCharsToBeChanged(plaintextCharacter)){
                return encrypt(plaintextCharacter, characters);
            } else {
                return plaintextCharacter;
            }
        }

        private static boolean isCharacterInTheAlphabetOfCharsToBeChanged(char plaintextCharacter) {
            return alphabetOfCipherCharacters.contains(plaintextCharacter);
        }

        private static Character encrypt(char plaintextCharacter, List<Character> characters) {
            int sizeOfTheAlphabet = characters.size();
            int encryptionOffset = sizeOfTheAlphabet / 2;
            int offsetOfPlaintextCharacter = characters.indexOf(plaintextCharacter);
            return characters.get((offsetOfPlaintextCharacter + encryptionOffset) % 6);
        }
    }
}
