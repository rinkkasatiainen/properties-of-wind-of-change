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
        String a = "A";

        String result = CesarCipher.encode(a);

        assertThat(result, equalTo("N"));

    }


    private String createARandomString(Character[] characters) {
        StringBuffer buffer = new StringBuffer();
            Random random = new Random();
        for( int i = 0; i < 100; i++){
            int index = random.nextInt(5);
            buffer.append(characters[index] );
        }
        return  buffer.toString();
    }

    private static class CesarCipher {

        private static List<Character> characters = Arrays.asList('A', 'B', 'C', 'N', 'O', 'P');

        public static String encode(String string){
            StringBuilder inProgress = new StringBuilder();
            for(int i = 0; i < string.length(); i++) {
                int encodedCharacter = string.charAt(i);
                if( characters.contains( (char) encodedCharacter )){
                    if(encodedCharacter >= 'N') {
                        encodedCharacter -= 13;
                    } else {
                        encodedCharacter += 13;
                    }
                }
                inProgress.append((char)encodedCharacter);
            }
            return inProgress.toString().toUpperCase();
        }
    }
}
