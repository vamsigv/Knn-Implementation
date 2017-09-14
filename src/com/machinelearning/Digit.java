package com.machinelearning;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author vamsi A digit will contain the pixel values contained in the array.
 *         (28*28) gray scale image. it has two attributes, 1. Digit 2. Pixel
 *         Data
 */
@Getter
@Setter
public class Digit {
    private int           number = -1;
    private List<Integer> pixels = new ArrayList<>();
}
