package gnu.kawa.xml;

import gnu.lists.AbstractSequence;
import gnu.lists.NodePredicate;
import gnu.lists.PositionConsumer;

public class AncestorAxis extends TreeScanner {
    public static AncestorAxis make(NodePredicate type) {
        AncestorAxis axis = new AncestorAxis();
        axis.type = type;
        return axis;
    }

    private static void scan(AbstractSequence seq, int ipos, int end, NodePredicate type, PositionConsumer out) {
        int ipos2 = seq.parentPos(ipos);
        if (ipos2 != end) {
            scan(seq, ipos2, end, type, out);
            if (type.isInstancePos(seq, ipos2)) {
                out.writePosition(seq, ipos2);
            }
        }
    }

    public void scan(AbstractSequence seq, int ipos, PositionConsumer out) {
        scan(seq, ipos, seq.endPos(), this.type, out);
    }
}
