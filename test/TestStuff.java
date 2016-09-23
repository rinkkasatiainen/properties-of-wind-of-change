import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestStuff {
    Character[] charactersOfAlphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'N', 'O', 'P', 'Q', 'R', 'S'};
    Character[] characterOfAlphabetAndSpecialCharacters = {'A', 'B', 'C', 'D', 'E', 'F', 'N', 'O', 'P', 'Q', 'R', 'S', ' ', '?' };

    @org.junit.Test
    public void should_be_original_when_encoding_twice() throws Exception {

        for( int i = 0; i < 100; i++){
            String plaintext = createARandomString(charactersOfAlphabet);
            String ciphertext = CesarCipher.encode(CesarCipher.encode(plaintext));

            assertThat(ciphertext, equalTo( plaintext ));
        }
    }

    @org.junit.Test
    public void special_characters_remain_the_same() throws Exception {

        for( int i = 0; i < 100; i++){
            String plaintext = createARandomString(characterOfAlphabetAndSpecialCharacters);
            String ciphertext = CesarCipher.encode(CesarCipher.encode(plaintext));

            assertThat(ciphertext, equalTo( plaintext ));
        }
    }

    @org.junit.Test
    public void should_have_only_uppercase_characters_when_encoded() throws Exception {

        List<Character> lowercaseChars = Lists.newArrayList(charactersOfAlphabet).stream().map((_char) -> _char.toLowerCase(_char) ).collect( Collectors.toList() ) ;

        for( int i = 0; i < 100; i++){
            String plaintext = createARandomString( lowercaseChars.toArray(new Character[]{}) );
            String ciphertext = CesarCipher.encode(plaintext);

            String ciphertext_of_uppercased_plaintext = CesarCipher.encode(plaintext.toUpperCase());

            assertThat(ciphertext, equalTo( ciphertext_of_uppercased_plaintext  ));
        }
    }

    @Test
    public void should_be_encoded_by_changing_the_alphabet_character_by_half_of_the_alphabet_size_default_being_13() throws Exception {

        for( int i = 0; i < 100; i++){
            String plaintext = getARandomStringOfLenght( charactersOfAlphabet, 1 );
            Character plaintextChar = plaintext.charAt(0);

            String ciphertext = CesarCipher.encode(plaintext);
            Character ciphertextChar = ciphertext.charAt(0);

            assertThat( Math.abs( ciphertextChar - plaintextChar ) , equalTo(13));
        }

    }

    @org.junit.Test
    public void aStringWithRandomAlphabetShouldNotBeEncodedAsSimilarlyAsStringWithDefaultAlphabet() throws Exception {
        List<Character> aRandomAlphabetToEncodeWith = randomizeTheEncryptionAlphabet();

        int failedCount = 0;
        int totalRounds = 100000;
        for(int i = 0; i < totalRounds; i++){
            String plaintext = createARandomString(charactersOfAlphabet);
            String ciphertextWithDefaultAlphabet = CesarCipher.encode(plaintext);
            String ciphertextWithRandomAlphabet = CesarCipher.encode(plaintext, aRandomAlphabetToEncodeWith);

            failedCount = ciphertextWithDefaultAlphabet.equals( ciphertextWithRandomAlphabet ) ?failedCount+1  : failedCount;

        }
        assertThat("Rounds: " + totalRounds + ", total of " + failedCount + " error occurred", failedCount < totalRounds / 100 , equalTo(true) );
    }

    @org.junit.Test
    public void shouldBeAbleToRandomizeTheAlphabetThatIsUsed() throws Exception {
        List<Character> characters = Arrays.asList(charactersOfAlphabet);
        Collections.shuffle( characters );

        for( int i = 0; i < 100; i++){
            String plaintext = createARandomString(characterOfAlphabetAndSpecialCharacters);
            String ciphertext = CesarCipher.encode(CesarCipher.encode(plaintext, characters), characters);

            assertThat("alphabet: " + Joiner.on(',').join( characters ) + "\n" + "plaintext:  " + plaintext + " - \n" + "ciphertext: " + ciphertext, ciphertext, equalTo( plaintext ));

        }
    }

    @org.junit.Test
    public void encodingWithTwoDifferentOffsetsIsNotTheSame() throws Exception {
        List<Character> characters = Arrays.asList(charactersOfAlphabet);

        for( int i = 0; i < 100; i++){
            String plaintext = createARandomString(characterOfAlphabetAndSpecialCharacters);
            String ciphertextWithOffsetOf3 = CesarCipher.encode(CesarCipher.encode(plaintext, characters), 3);
            String ciphertextWithOffsetOf4 = CesarCipher.encode(CesarCipher.encode(plaintext, characters), 4);
            String cipherWithDefaultOffset = CesarCipher.encode(CesarCipher.encode(plaintext, characters));

            assertThat( ciphertextWithOffsetOf3, not(equalTo(ciphertextWithOffsetOf4)));
            assertThat( ciphertextWithOffsetOf3, not(equalTo(cipherWithDefaultOffset)));
            assertThat( ciphertextWithOffsetOf4, not(equalTo(cipherWithDefaultOffset)));
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
        Random random = new Random();
        int stringLength = random.nextInt(100) + 1;
        return getARandomStringOfLenght(characters, stringLength);
    }

    private String getARandomStringOfLenght(Character[] alphabet, int stringLength) {
        Random random = new Random();
        String randomText = "";
        for( int i = 0; i < stringLength; i++){
            int index = random.nextInt(alphabet.length);
            randomText += alphabet[index];
        }
        return  randomText;
    }

    private static class CesarCipher {

        private static List<Character> alphabetOfCipherCharacters = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'N', 'O', 'P', 'Q', 'R', 'S');

        public static String encode(String plaintext){
            return encodeWithAlphabet(plaintext, alphabetOfCipherCharacters, rot13OffsetSupplier());
        }

        public static String encode(String plaintext, List<Character> alphabetListToEncrypt) {
            return encodeWithAlphabet(plaintext, alphabetListToEncrypt, rot13OffsetSupplier());
        }

        private static Supplier<Integer> rot13OffsetSupplier() {
            return () -> alphabetOfCipherCharacters.size() / 2;
        }

        public static String encode(String plaintext, int offset) {
            return encodeWithAlphabet(plaintext, alphabetOfCipherCharacters, () -> offset );
        }

        private static String encodeWithAlphabet(String plaintext, List<Character> alphabetOfCipherCharacters, Supplier<Integer> offsetSupplier) {
            String ciphertext = "";
            plaintext = plaintext.toUpperCase();
            for(int i = 0; i < plaintext.length(); i++) {
                char plaintextCharacter = plaintext.charAt(i);
                ciphertext += (encodeCharacter(plaintextCharacter, alphabetOfCipherCharacters, offsetSupplier));
            }
            return ciphertext;
        }

        private static Character encodeCharacter(char plaintextCharacter, List<Character> alphabetOfCharactersToBeCiphered, Supplier<Integer> offsetSupplier) {
            if(isCharacterInTheAlphabetOfCharsToBeChanged(plaintextCharacter, alphabetOfCharactersToBeCiphered)){
                return encrypt(plaintextCharacter, alphabetOfCharactersToBeCiphered, offsetSupplier);
            } else {
                return plaintextCharacter;
            }
        }

        private static boolean isCharacterInTheAlphabetOfCharsToBeChanged(char plaintextCharacter, List<Character> characters) {
            return characters.contains(plaintextCharacter);
        }

        private static Character encrypt(char plaintextCharacter, List<Character> characters, Supplier<Integer> offsetSupplier) {
            int sizeOfTheAlphabet = characters.size();
            int encryptionOffset = offsetSupplier.get();
            int offsetOfPlaintextCharacter = characters.indexOf(plaintextCharacter);
            return characters.get((offsetOfPlaintextCharacter + encryptionOffset) % sizeOfTheAlphabet);
        }
    }
}
