package com.youxing.sogoteacher.views;

import com.youxing.sogoteacher.model.Sku;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jun Deng on 15/8/13.
 */
public class StepperGroup {

    private Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();

    private int min;
    private int max;
    private int total;

    private int selectAdultNum;
    private int selectChildNum;
    private double totalPrice;

    public Map<Integer, Integer> getCountMap() {
        return countMap;
    }

    public void clear() {
        countMap.clear();
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSelectAdultNum() {
        return selectAdultNum;
    }

    public int getSelectChildNum() {
        return selectChildNum;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void compute(List<Sku.Price> skuPrices) {
        double totalPrice = 0;
        int adultNum = 0;
        int childNum = 0;
        for (int i = 0; i < skuPrices.size(); i++) {
            Sku.Price price = skuPrices.get(i);
            Integer count = countMap.get(i);
            if (count != null) {
                totalPrice += price.getPrice() * count;
                adultNum += price.getAdult() * count;
                childNum += price.getChild() * count;
            }
        }
        this.selectAdultNum = adultNum;
        this.selectChildNum = childNum;
        this.totalPrice = totalPrice;
    }

    public void adjustStepper(StepperView stepper) {
        updateTotal();
        stepper.setMax(max - total + stepper.getNumber());
    }

    private void updateTotal() {
        total = 0;
        for (Integer count : countMap.values()) {
            total += count;
        }
    }
}
