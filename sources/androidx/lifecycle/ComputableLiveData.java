package androidx.lifecycle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.VisibleForTesting;
import androidx.annotation.WorkerThread;
import androidx.arch.core.executor.ArchTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class ComputableLiveData<T> {
    final AtomicBoolean mComputing;
    final Executor mExecutor;
    final AtomicBoolean mInvalid;
    @VisibleForTesting
    final Runnable mInvalidationRunnable;
    final LiveData<T> mLiveData;
    @VisibleForTesting
    final Runnable mRefreshRunnable;

    /* access modifiers changed from: protected */
    @WorkerThread
    public abstract T compute();

    public ComputableLiveData() {
        this(ArchTaskExecutor.getIOThreadExecutor());
    }

    public ComputableLiveData(@NonNull Executor executor) {
        this.mInvalid = new AtomicBoolean(true);
        this.mComputing = new AtomicBoolean(false);
        this.mRefreshRunnable = new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:3:0x000d  */
            @androidx.annotation.WorkerThread
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r7 = this;
                    r6 = 1
                    r5 = 0
                L_0x0002:
                    r0 = 0
                    androidx.lifecycle.ComputableLiveData r2 = androidx.lifecycle.ComputableLiveData.this
                    java.util.concurrent.atomic.AtomicBoolean r2 = r2.mComputing
                    boolean r2 = r2.compareAndSet(r5, r6)
                    if (r2 == 0) goto L_0x0032
                    r1 = 0
                L_0x000e:
                    androidx.lifecycle.ComputableLiveData r2 = androidx.lifecycle.ComputableLiveData.this     // Catch:{ all -> 0x003f }
                    java.util.concurrent.atomic.AtomicBoolean r2 = r2.mInvalid     // Catch:{ all -> 0x003f }
                    r3 = 1
                    r4 = 0
                    boolean r2 = r2.compareAndSet(r3, r4)     // Catch:{ all -> 0x003f }
                    if (r2 == 0) goto L_0x0022
                    r0 = 1
                    androidx.lifecycle.ComputableLiveData r2 = androidx.lifecycle.ComputableLiveData.this     // Catch:{ all -> 0x003f }
                    java.lang.Object r1 = r2.compute()     // Catch:{ all -> 0x003f }
                    goto L_0x000e
                L_0x0022:
                    if (r0 == 0) goto L_0x002b
                    androidx.lifecycle.ComputableLiveData r2 = androidx.lifecycle.ComputableLiveData.this     // Catch:{ all -> 0x003f }
                    androidx.lifecycle.LiveData<T> r2 = r2.mLiveData     // Catch:{ all -> 0x003f }
                    r2.postValue(r1)     // Catch:{ all -> 0x003f }
                L_0x002b:
                    androidx.lifecycle.ComputableLiveData r2 = androidx.lifecycle.ComputableLiveData.this
                    java.util.concurrent.atomic.AtomicBoolean r2 = r2.mComputing
                    r2.set(r5)
                L_0x0032:
                    if (r0 == 0) goto L_0x003e
                    androidx.lifecycle.ComputableLiveData r2 = androidx.lifecycle.ComputableLiveData.this
                    java.util.concurrent.atomic.AtomicBoolean r2 = r2.mInvalid
                    boolean r2 = r2.get()
                    if (r2 != 0) goto L_0x0002
                L_0x003e:
                    return
                L_0x003f:
                    r2 = move-exception
                    androidx.lifecycle.ComputableLiveData r3 = androidx.lifecycle.ComputableLiveData.this
                    java.util.concurrent.atomic.AtomicBoolean r3 = r3.mComputing
                    r3.set(r5)
                    throw r2
                */
                throw new UnsupportedOperationException("Method not decompiled: androidx.lifecycle.ComputableLiveData.AnonymousClass2.run():void");
            }
        };
        this.mInvalidationRunnable = new Runnable() {
            @MainThread
            public void run() {
                boolean isActive = ComputableLiveData.this.mLiveData.hasActiveObservers();
                if (ComputableLiveData.this.mInvalid.compareAndSet(false, true) && isActive) {
                    ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
                }
            }
        };
        this.mExecutor = executor;
        this.mLiveData = new LiveData<T>() {
            /* access modifiers changed from: protected */
            public void onActive() {
                ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
            }
        };
    }

    @NonNull
    public LiveData<T> getLiveData() {
        return this.mLiveData;
    }

    public void invalidate() {
        ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
    }
}
