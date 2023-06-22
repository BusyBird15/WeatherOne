package defpackage;

import com.KIO4_TransportNet.KIO4_TransportNet;

/* renamed from: b  reason: default package */
public final class b implements Runnable {
    private /* synthetic */ KIO4_TransportNet a;

    public b(KIO4_TransportNet kIO4_TransportNet) {
        this.a = kIO4_TransportNet;
    }

    public final void run() {
        this.a.CheckTransport(this.a.salida);
    }
}
