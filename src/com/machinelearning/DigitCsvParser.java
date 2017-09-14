package com.machinelearning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.Map;

import com.opencsv.CSVReader;

import lombok.Getter;


@Getter
public class DigitCsvParser {

    private static List<Digit> listOfTrainingDigits;
    private static List<Digit> listOfTestingDigits;
    private static String      trainSourcePath = "/Users/vamsi/Documents/workspace/MachineLearning/src/data/train.csv";
    private static String      testSourcePath  = "/Users/vamsi/Documents/workspace/MachineLearning/src/data/test.csv";

    public static void initialise() throws FileNotFoundException {
        loadTestDataSet();
        loadTraningDataSet();
    }

    private static void loadTraningDataSet() throws FileNotFoundException {
        if (Objects.isNull(listOfTrainingDigits)) {
            listOfTrainingDigits = new ArrayList<>();
        }
        parseCsv(trainSourcePath, listOfTrainingDigits);
    }

    private static void loadTestDataSet() throws FileNotFoundException {
        if (Objects.isNull(listOfTestingDigits)) {
            listOfTestingDigits = new ArrayList<>();
        }
        parseCsv(testSourcePath, listOfTestingDigits);
    }

    private static void parseCsv(String filePath, List<Digit> digits) throws FileNotFoundException {
        CSVReader csvReader = new CSVReader(new FileReader(new File(filePath)), ',');
        Iterator<String[]> digitIterator = csvReader.iterator();
        digitIterator.next(); // skip the first line
        while (digitIterator.hasNext()) {
            int i = 0;
            int j = 0;
            String[] pixelArray = digitIterator.next();
            Digit digit = new Digit();
            if (pixelArray.length == 785) {
                digit.setNumber(Integer.parseInt(pixelArray[0]));
                i++;
            }
            for (; i < pixelArray.length; i++, j++) {
                digit.getPixels().add(j, Integer.parseInt(pixelArray[i]));
            }
            digits.add(digit);
        }
    }

    public static void main(String[] args) {
        try {
            initialise();
            predictDigits();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 
     * @param digit
     * @return eucledian distance between the two digits
     */
    public static double getDistanceBetweenDigits(Digit digit1, Digit digit2) {
        double sum = 0;
        for (int i = 0; i < digit1.getPixels().size(); i++) {
            sum += Math.pow(Math.abs(digit1.getPixels().get(i) - digit2.getPixels().get(i)), 2);
        }
        return Math.sqrt(sum);
    }

    private static void predictDigits() {
        for (int i = 0; i < listOfTestingDigits.size(); i++) {
            double sum = 0;
            Map<Double,List<Integer>> distanceMap = new TreeMap();
            for (int j = 0; j < listOfTrainingDigits.size(); j++) {
                   sum = getDistanceBetweenDigits(listOfTestingDigits.get(i), listOfTrainingDigits.get(j));
                   List<Integer> digitList = distanceMap.get(sum);
                   if(Objects.isNull(digitList)){
                       digitList = new ArrayList<>();
                       digitList.add(listOfTrainingDigits.get(j).getNumber());
                   }else{
                       digitList.add(listOfTrainingDigits.get(j).getNumber());
                   }
                   distanceMap.put(sum, digitList);
            }
            predictDigit(distanceMap);
        }
    }

    
    private static void predictDigit(Map<Double, List<Integer>> distanceMap) {
        Map<Integer, Integer> countDistance = new HashMap<>();
        int count = 0;
         for(Map.Entry<Double, List<Integer>> entry : distanceMap.entrySet()) {
             if(count == 10) {
                 break;
             }
             List<Integer> values = entry.getValue();
             for(int i=0;i<values.size();i++){
                 countDistance.put(values.get(i), Objects.isNull(countDistance.get(values.get(i))) ? 1 : countDistance.get(values.get(i)) + 1);
             }
             count ++;
         }
         display(countDistance);
    }

    private static void display(Map<Integer, Integer> countDistance) {
        for(Map.Entry<Integer, Integer> entry : countDistance.entrySet()){
            System.out.print("(" + entry.getKey() + ":"  + entry.getValue() + ")  ");
        }
        System.out.println();
    }
}
