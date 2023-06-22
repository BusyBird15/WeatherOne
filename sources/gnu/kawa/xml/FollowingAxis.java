package gnu.kawa.xml;

import gnu.lists.AbstractSequence;
import gnu.lists.NodePredicate;
import gnu.lists.PositionConsumer;

public class FollowingAxis extends TreeScanner {
    public static FollowingAxis make(NodePredicate type) {
        FollowingAxis axis = new FollowingAxis();
        axis.type = type;
        return axis;
    }

    public void scan(AbstractSequence seq, int ipos, PositionConsumer out) {
        int limit = seq.endPos();
        int ipos2 = seq.nextPos(ipos);
        if (ipos2 != 0 && this.type.isInstancePos(seq, ipos2)) {
            out.writePosition(seq, ipos2);
        }
        while (true) {
            ipos2 = seq.nextMatching(ipos2, this.type, limit, true);
            if (ipos2 != 0) {
                out.writePosition(seq, ipos2);
            } else {
                return;
            }
        }
    }
}
