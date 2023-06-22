package gnu.kawa.models;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class WithTransform implements Paintable {
    Paintable paintable;
    AffineTransform transform;

    public WithTransform(Paintable paintable2, AffineTransform transform2) {
        this.paintable = paintable2;
        this.transform = transform2;
    }

    public void paint(Graphics2D graphics) {
        AffineTransform saved = graphics.getTransform();
        try {
            graphics.transform(this.transform);
            this.paintable.paint(graphics);
        } finally {
            graphics.setTransform(saved);
        }
    }

    public Rectangle2D getBounds2D() {
        return this.transform.createTransformedShape(this.paintable.getBounds2D()).getBounds2D();
    }

    public Paintable transform(AffineTransform tr) {
        AffineTransform combined = new AffineTransform(this.transform);
        combined.concatenate(tr);
        return new WithTransform(this.paintable, combined);
    }
}
