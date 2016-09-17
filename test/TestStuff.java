import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestStuff {

    @org.junit.Test
    public void shouldBeTheSameAfterEncodingTwice() throws Exception {
        Character[] characters = {'A', 'B', 'C', 'N', 'O', 'P'};

        for( int i = 0; i < 100; i++){
            String aRandomString = createARandomString(characters);
            String encode = CesarCipher.encode(CesarCipher.encode(aRandomString));

            assertThat(encode, equalTo( aRandomString ));
        }
    }

    @org.junit.Test
    public void shouldBeTheSameAfterEncodingTwiceWithSpecialChars() throws Exception {
        Character[] characters = {'A', 'B', 'C', 'N', 'O', 'P', ' ', '?' };

        for( int i = 0; i < 100; i++){
            String aRandomString = createARandomString(characters);
            String encode = CesarCipher.encode(CesarCipher.encode(aRandomString));

            assertThat(encode, equalTo( aRandomString ));
        }
    }

    @org.junit.Test
    public void shouldNotHaveLowercaseCharactersAfterEncoding() throws Exception {
        Character[] characters = {'a', 'b', 'c', 'n', 'o', 'p'};

        for( int i = 0; i < 100; i++){
            String aRandomString = createARandomString(characters);
            String encode = CesarCipher.encode(aRandomString);

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

        private static List<Character> characters = Arrays.asList('A', 'B', 'C', 'N', 'O', 'P');

        public static String encode(String string){
            StringBuilder inProgress = new StringBuilder();
            for(int i = 0; i < string.length(); i++) {
                int plaintextCharacter = string.charAt(i);
                Character toBeAppended;
                toBeAppended = encodeCharacter((char) plaintextCharacter);
                inProgress.append( toBeAppended);
            }
            return inProgress.toString().toUpperCase();
        }

        private static Character encodeCharacter(char plaintextCharacter) {
            if(isCharacterInTheAlphabetOfCharsToBeChanged(plaintextCharacter)){
                return new Character(encrypt(plaintextCharacter));
            } else {
                return new Character(plaintextCharacter);
            }
        }

        private static boolean isCharacterInTheAlphabetOfCharsToBeChanged(char plaintextCharacter) {
            return characters.contains(plaintextCharacter);
        }

        private static char encrypt(char plaintextCharacter) {
            int sizeOfTheAlphabet = characters.size();
            int encryptionOffset = sizeOfTheAlphabet / 2;
            int offsetOfPlaintextCharacter = characters.indexOf(plaintextCharacter);
            return (char) characters.get((offsetOfPlaintextCharacter + encryptionOffset) % 6);
        }

    }
}
