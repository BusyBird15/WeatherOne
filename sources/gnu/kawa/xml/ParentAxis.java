package gnu.kawa.xml;

import gnu.lists.AbstractSequence;
import gnu.lists.NodePredicate;
import gnu.lists.PositionConsumer;

public class ParentAxis extends TreeScanner {
    public static ParentAxis make(NodePredicate type) {
        ParentAxis axis = new ParentAxis();
        axis.type = type;
        return axis;
    }

    public void scan(AbstractSequence seq, int ipos, PositionConsumer out) {
        int ipos2 = seq.parentPos(ipos);
        if (ipos2 != seq.endPos() && this.type.isInstancePos(seq, ipos2)) {
            out.writePosition(seq, ipos2);
        }
    }
}
