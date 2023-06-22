package kawa.standard;

import gnu.lists.FVector;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Procedure;
import gnu.mapping.ProcedureN;
import gnu.mapping.WrongType;

public class vector_append extends ProcedureN {
    public static final vector_append vectorAppend = new vector_append("vector-append");

    public vector_append(String name) {
        super(name);
    }

    public Object applyN(Object[] args) {
        return apply$V(args);
    }

    public static FVector apply$V(Object[] args) {
        int length = 0;
        int args_length = args.length;
        int i = args_length;
        while (true) {
            i--;
            if (i >= 0) {
                Object arg = args[i];
                if (arg instanceof FVector) {
                    length += ((FVector) arg).size();
                } else {
                    int n = LList.listLength(arg, false);
                    if (n < 0) {
                        throw new WrongType((Procedure) vectorAppend, i, arg, "list or vector");
                    }
                    length += n;
                }
            } else {
                Object[] result = new Object[length];
                int position = 0;
                for (int i2 = 0; i2 < args_length; i2++) {
                    Object arg2 = args[i2];
                    if (arg2 instanceof FVector) {
                        FVector vec = (FVector) arg2;
                        int vec_length = vec.size();
                        int j = 0;
                        int position2 = position;
                        while (j < vec_length) {
                            result[position2] = vec.get(j);
                            j++;
                            position2++;
                        }
                        position = position2;
                    } else if (arg2 instanceof Pair) {
                        while (arg2 != LList.Empty) {
                            Pair pair = (Pair) arg2;
                            result[position] = pair.getCar();
                            arg2 = pair.getCdr();
                            position++;
                        }
                    }
                }
                return new FVector(result);
            }
        }
    }
}
