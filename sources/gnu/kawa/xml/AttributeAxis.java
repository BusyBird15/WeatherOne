package gnu.kawa.xml;

import gnu.lists.AbstractSequence;
import gnu.lists.NodePredicate;
import gnu.lists.PositionConsumer;

public class AttributeAxis extends TreeScanner {
    public static AttributeAxis make(NodePredicate type) {
        AttributeAxis axis = new AttributeAxis();
        axis.type = type;
        return axis;
    }

    public void scan(AbstractSequence seq, int ipos, PositionConsumer out) {
        int ipos2 = seq.firstAttributePos(ipos);
        while (ipos2 != 0 && seq.getNextKind(ipos2) == 35) {
            if (this.type.isInstancePos(seq, ipos2)) {
                out.writePosition(seq, ipos2);
            } else if (seq.getNextKind(ipos2) != 35) {
                return;
            }
            ipos2 = seq.nextPos(ipos2);
        }
    }
}
