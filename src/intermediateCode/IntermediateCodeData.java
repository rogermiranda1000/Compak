package intermediateCode;

import entities.Token;
import entities.TokenDataPair;

import java.util.ArrayList;

public class IntermediateCodeData {

    /**
     * Example 3@Code Representation
     *      Op     Arg1     Arg2     Result
     * 0:   -      c                   t1
     * 1:   +      b          t1       t2
     * 2:   =      d          t2        a
     */

    private ArrayList<ThreeAddressLine> data;

    public IntermediateCodeData() {
        data = new ArrayList<>();
    }

    public void addLine(TokenDataPair op, Object arg1, Object arg2) {
        data.add(new ThreeAddressLine(op, arg1, arg2));
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(ThreeAddressLine d : data){
            s.append(d.toString());
        }
        return s.toString();
    }
}
