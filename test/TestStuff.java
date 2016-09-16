import org.junit.Test;

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


    @Test
    public void testName() throws Exception {
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
        public static String encode(String string){
            StringBuilder inProgress = new StringBuilder();
            for(int i = 0; i < string.length(); i++) {
                int encodedCharacter = string.charAt(i);
                if(encodedCharacter >= 'N') {
                    encodedCharacter -= 13;
                } else {
                    encodedCharacter += 13;
                }
                inProgress.append((char)encodedCharacter);
            }
            return inProgress.toString();
        }
    }
}
