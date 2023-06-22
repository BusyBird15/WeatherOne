package gnu.lists;

public abstract class ExtSequence extends AbstractSequence {
    public int copyPos(int ipos) {
        return ipos <= 0 ? ipos : PositionManager.manager.register(PositionManager.getPositionObject(ipos).copy());
    }

    /* access modifiers changed from: protected */
    public void releasePos(int ipos) {
        if (ipos > 0) {
            PositionManager.manager.release(ipos);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isAfterPos(int ipos) {
        if (ipos <= 0) {
            if (ipos < 0) {
                return true;
            }
            return false;
        } else if ((PositionManager.getPositionObject(ipos).ipos & 1) == 0) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public int nextIndex(int ipos) {
        if (ipos == -1) {
            return size();
        }
        if (ipos == 0) {
            return 0;
        }
        return PositionManager.getPositionObject(ipos).nextIndex();
    }
}
