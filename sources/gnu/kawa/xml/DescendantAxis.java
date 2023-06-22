package gnu.kawa.xml;

import gnu.lists.AbstractSequence;
import gnu.lists.NodePredicate;
import gnu.lists.PositionConsumer;
import gnu.lists.TreeList;

public class DescendantAxis extends TreeScanner {
    public static DescendantAxis make(NodePredicate type) {
        DescendantAxis axis = new DescendantAxis();
        axis.type = type;
        return axis;
    }

    public void scan(AbstractSequence seq, int ipos, PositionConsumer out) {
        if (!(seq instanceof TreeList)) {
            int ipos2 = seq.firstChildPos(ipos);
            while (ipos2 != 0) {
                if (this.type.isInstancePos(seq, ipos2)) {
                    out.writePosition(seq, ipos2);
                }
                scan(seq, ipos2, out);
                ipos2 = seq.nextPos(ipos2);
            }
            return;
        }
        int limit = seq.nextPos(ipos);
        int child = ipos;
        while (true) {
            child = seq.nextMatching(child, this.type, limit, true);
            if (child != 0) {
                out.writePosition(seq, child);
            } else {
                return;
            }
        }
    }
}
