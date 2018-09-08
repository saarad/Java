
/**
 * HammingDecoder.java
 * @author Ilia Rad Saadat
 *
 * This class uses the Hamming (7,4) to decode and code 4 bit-squenses and
 * find mistakes.
 * It creates new matrices of the sequence and given standard matrices.
 * It will then find the right position where the mistake occured
 * and switch the bit.
 * This is part of an solution to a math exercise.
 */
public class HammingDecoder {
    private final int bitSequence = 4;
    private final int[][] matrixG = new int[7][4];
    private final int[][] matrixH = new int[3][7];

    /**
     * Setting the standard matrices in
     * the constructor
     */
    public HammingDecoder(){
        //Matrix G
        for(int i = 0; i<3;i++){
            matrixG[i][0] = 1;
        }//end loop
        for(int i = 3; i<7; i++){
            matrixG[i][0] = 0;
        }//end loop
        matrixG[0][1] = 1;
        for(int i = 1; i<3;i++){
            matrixG[i][1] = 0;
        }//end loop
        for(int i = 3;i<5;i++){
            matrixG[i][1] = 1;
        }//end loop
        for(int i = 5; i<7;i++){
            matrixG[i][1] = 0;
        }//end loop
        int insertG = 0;
        for(int i = 0; i<7;i++){ //alternate
            matrixG[i][2] = insertG;
            if(insertG==0)insertG = 1;
            else insertG = 0;
        }//end loop
        for(int i = 0; i<2;i++){
            matrixG[i][3] = 1;
        }//end loop
        matrixG[2][3] = 0;
        matrixG[3][3] = 1;
        matrixG[6][3] = 1;
        for(int i = 4; i<6;i++){
            matrixG[i][3] = 0;
        }//end loop

        //Matrix H
        int insertH = 1;
        for(int i = 0; i<7;i++){ //alternate
            matrixH[0][i] = insertH;
            if(insertH == 1)insertH = 0;
            else insertH = 1;
        }//end loop
        matrixH[1][0] = 0;
        int counterH = 0;
        int insertH1 = 1;
        for(int i = 1; i<7;i++){
            matrixH[1][i] = insertH1;
            counterH++;
            if(counterH%2 == 0){
                if(insertH1 == 1)insertH1 = 0;
                else insertH1 = 1;
            }//end condition
        }//end loop
        for(int i = 0; i<3;i++){
            matrixH[2][i] = 0;
        }//end loop
        for(int i = 3; i<7; i++){
            matrixH[2][i] = 1;
        }//end loop
    }//end constructor

    //Access methods
    public int[][] getMatrixG(){return matrixG;}
    public int[][] getMatrixH(){return matrixH;}

    /**
     * Help-method to multiply the standard matrices with
     * the matrices we want to multiply
     * @param standardMatrix is one of the standard matrices
     * @param matrix is the matrix we want to multiply
     * @return the new matrix
     */
    private int[] multiplyMatrices(int[][]standardMatrix, int[] matrix){
        if(standardMatrix[0].length != matrix.length)throw new IllegalArgumentException("This matrices aren't" +
                " possible to multiply!");
        int[] result = new int[standardMatrix.length];
        for(int i = 0; i<result.length; i++){//perform multiplication
            int insert = 0;
            for(int j = 0; j<matrix.length; j++){
                insert += standardMatrix[i][j]*matrix[j];
            }//end loop
            result[i] = insert;
        }//end loop
        return result;
    }//end method

    /**
     * Help-method to change one matrix to a bit-sequence
     * @param matrix is the matrix we want to change
     * @return the changed matrix
     */
    private int[] changeToBit(int[] matrix){
        int[] result = new int[matrix.length];
        for(int i = 0; i<result.length; i++){
            if(matrix[i]%2==0 || matrix[i] == 0)result[i] = 0;
            else result[i] = 1;
        }//end loop
        return result;
    }//end method

    /**
     * Help-method to check if one matrix matches a part of
     * the matrix H
     * @param matrix is the matrix we're checking
     * @return the first index of the line that matches.
     * If the value is -1 it means no matches were found
     */
    private int checkMatrices(int[] matrix){
        int index = -1;
        for(int i = 0; i<matrixH[0].length;i++){
            int count = 0;
            for(int j = 0; j<matrixH.length;j++){
                if(matrix[j] == matrixH[j][i])count++;
            }//end loop
            if(count == 3){
                index = i;
            }//end condition
        }//end loop
        return index;
    }//end method

    /**
     * Method to code a bit-sequence
     * @param bitSequence is the bit-sequence we want to code
     * @return the coded sequence
     */
    public int[] codeSequence(int[] bitSequence){
        if(bitSequence.length != 4)throw new IllegalArgumentException("It must be 4 bits!");
        int[] k = multiplyMatrices(matrixG,bitSequence);
        return changeToBit(k);
    }//end method

    /**
     * Method to decode a sequence and find mistakes
     * @param bitSequence is the bit-sequence the method examine
     * @return the fixed sequence or -1 if something went wrong
     */
    public int[] decodeSequence(int[] bitSequence){
        if(bitSequence.length != 7)throw new IllegalArgumentException("" +
                "The code is only designed to check 7-bit sequences");
        int[] k = inverted(bitSequence);
        int[] hk = multiplyMatrices(matrixH,k);
        int[] e = changeToBit(hk);
        boolean correctMatrix = false;
        int count = 0;
        for(int i = 0; i<hk.length;i++){
            if(e[i] == 0)count++;
        }//end loop
        if(count == 3) correctMatrix = true;
        if(correctMatrix)return k; //nothing to correct

        int index = checkMatrices(e);
        if(index != -1){
            if(k[index]==0)k[index] = 1;
            else if(k[index]==1)k[index] = 0;
            return k;
        }else{ //something went wrong
            int[] error = {-1};
            return error;
        }//end condition
    }//end method

    /**
     * Method to invert matrices
     * @param matrix is the matrix we want to invert
     * @return inverted matrix
     */
    private int[] inverted(int[] matrix){
        int[] result = new int[matrix.length];
        for(int i = 0; i<result.length;i++){ //inverting
            if(matrix[i] == 0)result[i] = 1;
            else result[i] = 0;
        }//end loop
        return result;
    }//end method

    /**
     * Method to find the coded sequence from the decoded one
     * @param correctSequence is the corrected sequence
     * @return the coded sequence
     */
    public int[] getOriginalSequence(int[] correctSequence){
        if(correctSequence.length != 7)throw new IllegalArgumentException("The sequence must be 7 bits");
        int[] invert = inverted(correctSequence);
        int[] result = {invert[2],invert[4],invert[5],invert[6]};
        return result;
    }//end method





    //Testing
    public static void main(String[] args){
        HammingDecoder decoder = new HammingDecoder();
        System.out.println("Matrix G: ");
        //Printing out the G matrix
        for(int i = 0;i<7;i++){
            for(int j = 0; j<4;j++){
                System.out.println(decoder.getMatrixG()[i][j]);
            }//end loop
            System.out.println("\n");
        }//end loop
        System.out.println("\nMatrix H: ");
        //Printing out the H matrix
        for(int i = 0; i<3;i++){
            for(int j = 0; j<7;j++){
                System.out.println(decoder.getMatrixH()[i][j]);
            }//end loop
            System.out.println("\n");
        }//end loop

        //Testing the multiply-method
        int[] test = {1,0,1,0};
        System.out.println("Multiplying with matrix G: ");
        int[] out = decoder.multiplyMatrices(decoder.getMatrixG(),test);
        for(int i = 0; i<out.length; i++){
            System.out.println(out[i]);
        }//end loop

        int[] test2 = {1,0,1,1,1,1,0};
        System.out.println("\nMultiplying with matrix H: ");
        int[] out2 = decoder.multiplyMatrices(decoder.getMatrixH(),test2);
        for(int i = 0; i<out2.length;i++){
            System.out.println(out2[i]);
        }//end loop

        //Testing the change-method
        System.out.println("Changing the first matrix: ");
        int[] out3 = decoder.changeToBit(out);
        for(int i = 0; i<out3.length;i++){
            System.out.println(out3[i]);
        }//end loop
        System.out.println("\nChanging the second matrix");
        int[] out4 = decoder.changeToBit(out2);
        for(int i = 0; i<out4.length;i++){
            System.out.println(out4[i]);
        }//end loop

        //Testing the checking-method
        System.out.println("Trying the error-searcher, should give 4: ");
        System.out.println(decoder.checkMatrices(out4));

        //Testing the system
        System.out.println("Trying the coding: ");
        int[] bits = {1,0,1,1};
        int[] outs = decoder.codeSequence(bits);
        for(int i = 0; i<outs.length; i++) {
            System.out.println(outs[i]);
        }//end loop


        System.out.println("Trying the whole system, " +
                "should give 0101010: ");
        int[] bitSequence = {1,0,1,1,1,0,1};
        int[] decoded = decoder.decodeSequence(bitSequence);
        for(int i = 0;i<decoded.length;i++){
            System.out.println(decoded[i]);
        }//end loop
        System.out.println("Finding the coded sequence, 1101");
        int[] original1 = decoder.getOriginalSequence(decoded);
        for(int i = 0; i<original1.length;i++){
            System.out.println(original1[i]);
        }//end loop

        System.out.println("Trying the whole system, should give 1001100");
        int[] bitSequence2 = {0,1,0,0,0,1,1};
        int[] decoded2 = decoder.decodeSequence(bitSequence2);
        for(int i = 0; i<decoded.length;i++){
            System.out.println(decoded2[i]);
        }//end loop
        System.out.println("Finding the coded sequence, 1011");
        int[] original = decoder.getOriginalSequence(decoded2);
        for(int i = 0; i<original.length;i++){
            System.out.println(original[i]);
        }//end loop
    }//end main
}//end class
