package androidx.fragment.app;

import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.app.SharedElementCallback;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.BackStackRecord;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class FragmentTransition {
    private static final int[] INVERSE_OPS = {0, 3, 0, 1, 5, 4, 7, 6, 9, 8};
    private static final FragmentTransitionImpl PLATFORM_IMPL = (Build.VERSION.SDK_INT >= 21 ? new FragmentTransitionCompat21() : null);
    private static final FragmentTransitionImpl SUPPORT_IMPL = resolveSupportImpl();

    private static FragmentTransitionImpl resolveSupportImpl() {
        try {
            return (FragmentTransitionImpl) Class.forName("androidx.transition.FragmentTransitionSupport").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            return null;
        }
    }

    static void startTransitions(FragmentManagerImpl fragmentManager, ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex, boolean isReordered) {
        if (fragmentManager.mCurState >= 1) {
            SparseArray<FragmentContainerTransition> transitioningFragments = new SparseArray<>();
            for (int i = startIndex; i < endIndex; i++) {
                BackStackRecord record = records.get(i);
                if (isRecordPop.get(i).booleanValue()) {
                    calculatePopFragments(record, transitioningFragments, isReordered);
                } else {
                    calculateFragments(record, transitioningFragments, isReordered);
                }
            }
            if (transitioningFragments.size() != 0) {
                View nonExistentView = new View(fragmentManager.mHost.getContext());
                int numContainers = transitioningFragments.size();
                for (int i2 = 0; i2 < numContainers; i2++) {
                    int containerId = transitioningFragments.keyAt(i2);
                    ArrayMap<String, String> nameOverrides = calculateNameOverrides(containerId, records, isRecordPop, startIndex, endIndex);
                    FragmentContainerTransition containerTransition = transitioningFragments.valueAt(i2);
                    if (isReordered) {
                        configureTransitionsReordered(fragmentManager, containerId, containerTransition, nonExistentView, nameOverrides);
                    } else {
                        configureTransitionsOrdered(fragmentManager, containerId, containerTransition, nonExistentView, nameOverrides);
                    }
                }
            }
        }
    }

    private static ArrayMap<String, String> calculateNameOverrides(int containerId, ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        ArrayList<String> sources;
        ArrayList<String> targets;
        ArrayMap<String, String> nameOverrides = new ArrayMap<>();
        for (int recordNum = endIndex - 1; recordNum >= startIndex; recordNum--) {
            BackStackRecord record = records.get(recordNum);
            if (record.interactsWith(containerId)) {
                boolean isPop = isRecordPop.get(recordNum).booleanValue();
                if (record.mSharedElementSourceNames != null) {
                    int numSharedElements = record.mSharedElementSourceNames.size();
                    if (isPop) {
                        targets = record.mSharedElementSourceNames;
                        sources = record.mSharedElementTargetNames;
                    } else {
                        sources = record.mSharedElementSourceNames;
                        targets = record.mSharedElementTargetNames;
                    }
                    for (int i = 0; i < numSharedElements; i++) {
                        String sourceName = sources.get(i);
                        String targetName = targets.get(i);
                        String previousTarget = nameOverrides.remove(targetName);
                        if (previousTarget != null) {
                            nameOverrides.put(sourceName, previousTarget);
                        } else {
                            nameOverrides.put(sourceName, targetName);
                        }
                    }
                }
            }
        }
        return nameOverrides;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x001a, code lost:
        r15 = r33.lastIn;
        r29 = r33.firstOut;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void configureTransitionsReordered(androidx.fragment.app.FragmentManagerImpl r31, int r32, androidx.fragment.app.FragmentTransition.FragmentContainerTransition r33, android.view.View r34, androidx.collection.ArrayMap<java.lang.String, java.lang.String> r35) {
        /*
            r3 = 0
            r0 = r31
            androidx.fragment.app.FragmentContainer r4 = r0.mContainer
            boolean r4 = r4.onHasView()
            if (r4 == 0) goto L_0x0017
            r0 = r31
            androidx.fragment.app.FragmentContainer r4 = r0.mContainer
            r0 = r32
            android.view.View r3 = r4.onFindViewById(r0)
            android.view.ViewGroup r3 = (android.view.ViewGroup) r3
        L_0x0017:
            if (r3 != 0) goto L_0x001a
        L_0x0019:
            return
        L_0x001a:
            r0 = r33
            androidx.fragment.app.Fragment r15 = r0.lastIn
            r0 = r33
            androidx.fragment.app.Fragment r0 = r0.firstOut
            r29 = r0
            r0 = r29
            androidx.fragment.app.FragmentTransitionImpl r2 = chooseImpl(r0, r15)
            if (r2 == 0) goto L_0x0019
            r0 = r33
            boolean r0 = r0.lastInIsPop
            r16 = r0
            r0 = r33
            boolean r0 = r0.firstOutIsPop
            r30 = r0
            java.util.ArrayList r8 = new java.util.ArrayList
            r8.<init>()
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            r0 = r16
            java.lang.Object r9 = getEnterTransition(r2, r15, r0)
            r0 = r29
            r1 = r30
            java.lang.Object r10 = getExitTransition(r2, r0, r1)
            r4 = r34
            r5 = r35
            r6 = r33
            java.lang.Object r14 = configureSharedElementsReordered(r2, r3, r4, r5, r6, r7, r8, r9, r10)
            if (r9 != 0) goto L_0x0060
            if (r14 != 0) goto L_0x0060
            if (r10 == 0) goto L_0x0019
        L_0x0060:
            r0 = r29
            r1 = r34
            java.util.ArrayList r22 = configureEnteringExitingViews(r2, r10, r0, r7, r1)
            r0 = r34
            java.util.ArrayList r20 = configureEnteringExitingViews(r2, r9, r15, r8, r0)
            r4 = 4
            r0 = r20
            setViewVisibility(r0, r4)
            r11 = r2
            r12 = r9
            r13 = r10
            java.lang.Object r18 = mergeTransitions(r11, r12, r13, r14, r15, r16)
            if (r18 == 0) goto L_0x0019
            r0 = r29
            r1 = r22
            replaceHide(r2, r10, r0, r1)
            java.util.ArrayList r27 = r2.prepareSetNameOverridesReordered(r8)
            r17 = r2
            r19 = r9
            r21 = r10
            r23 = r14
            r24 = r8
            r17.scheduleRemoveTargets(r18, r19, r20, r21, r22, r23, r24)
            r0 = r18
            r2.beginDelayedTransition(r3, r0)
            r23 = r2
            r24 = r3
            r25 = r7
            r26 = r8
            r28 = r35
            r23.setNameOverridesReordered(r24, r25, r26, r27, r28)
            r4 = 0
            r0 = r20
            setViewVisibility(r0, r4)
            r2.swapSharedElementTargets(r14, r7, r8)
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentTransition.configureTransitionsReordered(androidx.fragment.app.FragmentManagerImpl, int, androidx.fragment.app.FragmentTransition$FragmentContainerTransition, android.view.View, androidx.collection.ArrayMap):void");
    }

    private static void replaceHide(FragmentTransitionImpl impl, Object exitTransition, Fragment exitingFragment, final ArrayList<View> exitingViews) {
        if (exitingFragment != null && exitTransition != null && exitingFragment.mAdded && exitingFragment.mHidden && exitingFragment.mHiddenChanged) {
            exitingFragment.setHideReplaced(true);
            impl.scheduleHideFragmentView(exitTransition, exitingFragment.getView(), exitingViews);
            OneShotPreDrawListener.add(exitingFragment.mContainer, new Runnable() {
                public void run() {
                    FragmentTransition.setViewVisibility(exitingViews, 4);
                }
            });
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x001a, code lost:
        r15 = r36.lastIn;
        r32 = r36.firstOut;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void configureTransitionsOrdered(androidx.fragment.app.FragmentManagerImpl r34, int r35, androidx.fragment.app.FragmentTransition.FragmentContainerTransition r36, android.view.View r37, androidx.collection.ArrayMap<java.lang.String, java.lang.String> r38) {
        /*
            r3 = 0
            r0 = r34
            androidx.fragment.app.FragmentContainer r4 = r0.mContainer
            boolean r4 = r4.onHasView()
            if (r4 == 0) goto L_0x0017
            r0 = r34
            androidx.fragment.app.FragmentContainer r4 = r0.mContainer
            r0 = r35
            android.view.View r3 = r4.onFindViewById(r0)
            android.view.ViewGroup r3 = (android.view.ViewGroup) r3
        L_0x0017:
            if (r3 != 0) goto L_0x001a
        L_0x0019:
            return
        L_0x001a:
            r0 = r36
            androidx.fragment.app.Fragment r15 = r0.lastIn
            r0 = r36
            androidx.fragment.app.Fragment r0 = r0.firstOut
            r32 = r0
            r0 = r32
            androidx.fragment.app.FragmentTransitionImpl r2 = chooseImpl(r0, r15)
            if (r2 == 0) goto L_0x0019
            r0 = r36
            boolean r0 = r0.lastInIsPop
            r31 = r0
            r0 = r36
            boolean r0 = r0.firstOutIsPop
            r33 = r0
            r0 = r31
            java.lang.Object r9 = getEnterTransition(r2, r15, r0)
            r0 = r32
            r1 = r33
            java.lang.Object r10 = getExitTransition(r2, r0, r1)
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            java.util.ArrayList r8 = new java.util.ArrayList
            r8.<init>()
            r4 = r37
            r5 = r38
            r6 = r36
            java.lang.Object r14 = configureSharedElementsOrdered(r2, r3, r4, r5, r6, r7, r8, r9, r10)
            if (r9 != 0) goto L_0x0060
            if (r14 != 0) goto L_0x0060
            if (r10 == 0) goto L_0x0019
        L_0x0060:
            r0 = r32
            r1 = r37
            java.util.ArrayList r21 = configureEnteringExitingViews(r2, r10, r0, r7, r1)
            if (r21 == 0) goto L_0x0070
            boolean r4 = r21.isEmpty()
            if (r4 == 0) goto L_0x0071
        L_0x0070:
            r10 = 0
        L_0x0071:
            r0 = r37
            r2.addTarget(r9, r0)
            r0 = r36
            boolean r0 = r0.lastInIsPop
            r16 = r0
            r11 = r2
            r12 = r9
            r13 = r10
            java.lang.Object r17 = mergeTransitions(r11, r12, r13, r14, r15, r16)
            if (r17 == 0) goto L_0x0019
            java.util.ArrayList r19 = new java.util.ArrayList
            r19.<init>()
            r16 = r2
            r18 = r9
            r20 = r10
            r22 = r14
            r23 = r8
            r16.scheduleRemoveTargets(r17, r18, r19, r20, r21, r22, r23)
            r22 = r2
            r23 = r3
            r24 = r15
            r25 = r37
            r26 = r8
            r27 = r9
            r28 = r19
            r29 = r10
            r30 = r21
            scheduleTargetChange(r22, r23, r24, r25, r26, r27, r28, r29, r30)
            r0 = r38
            r2.setNameOverridesOrdered(r3, r8, r0)
            r0 = r17
            r2.beginDelayedTransition(r3, r0)
            r0 = r38
            r2.scheduleNameReset(r3, r8, r0)
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentTransition.configureTransitionsOrdered(androidx.fragment.app.FragmentManagerImpl, int, androidx.fragment.app.FragmentTransition$FragmentContainerTransition, android.view.View, androidx.collection.ArrayMap):void");
    }

    private static void scheduleTargetChange(FragmentTransitionImpl impl, ViewGroup sceneRoot, Fragment inFragment, View nonExistentView, ArrayList<View> sharedElementsIn, Object enterTransition, ArrayList<View> enteringViews, Object exitTransition, ArrayList<View> exitingViews) {
        final Object obj = enterTransition;
        final FragmentTransitionImpl fragmentTransitionImpl = impl;
        final View view = nonExistentView;
        final Fragment fragment = inFragment;
        final ArrayList<View> arrayList = sharedElementsIn;
        final ArrayList<View> arrayList2 = enteringViews;
        final ArrayList<View> arrayList3 = exitingViews;
        final Object obj2 = exitTransition;
        OneShotPreDrawListener.add(sceneRoot, new Runnable() {
            public void run() {
                if (obj != null) {
                    fragmentTransitionImpl.removeTarget(obj, view);
                    arrayList2.addAll(FragmentTransition.configureEnteringExitingViews(fragmentTransitionImpl, obj, fragment, arrayList, view));
                }
                if (arrayList3 != null) {
                    if (obj2 != null) {
                        ArrayList<View> tempExiting = new ArrayList<>();
                        tempExiting.add(view);
                        fragmentTransitionImpl.replaceTargets(obj2, arrayList3, tempExiting);
                    }
                    arrayList3.clear();
                    arrayList3.add(view);
                }
            }
        });
    }

    private static FragmentTransitionImpl chooseImpl(Fragment outFragment, Fragment inFragment) {
        ArrayList<Object> transitions = new ArrayList<>();
        if (outFragment != null) {
            Object exitTransition = outFragment.getExitTransition();
            if (exitTransition != null) {
                transitions.add(exitTransition);
            }
            Object returnTransition = outFragment.getReturnTransition();
            if (returnTransition != null) {
                transitions.add(returnTransition);
            }
            Object sharedReturnTransition = outFragment.getSharedElementReturnTransition();
            if (sharedReturnTransition != null) {
                transitions.add(sharedReturnTransition);
            }
        }
        if (inFragment != null) {
            Object enterTransition = inFragment.getEnterTransition();
            if (enterTransition != null) {
                transitions.add(enterTransition);
            }
            Object reenterTransition = inFragment.getReenterTransition();
            if (reenterTransition != null) {
                transitions.add(reenterTransition);
            }
            Object sharedEnterTransition = inFragment.getSharedElementEnterTransition();
            if (sharedEnterTransition != null) {
                transitions.add(sharedEnterTransition);
            }
        }
        if (transitions.isEmpty()) {
            return null;
        }
        if (PLATFORM_IMPL != null && canHandleAll(PLATFORM_IMPL, transitions)) {
            return PLATFORM_IMPL;
        }
        if (SUPPORT_IMPL != null && canHandleAll(SUPPORT_IMPL, transitions)) {
            return SUPPORT_IMPL;
        }
        if (PLATFORM_IMPL == null && SUPPORT_IMPL == null) {
            return null;
        }
        throw new IllegalArgumentException("Invalid Transition types");
    }

    private static boolean canHandleAll(FragmentTransitionImpl impl, List<Object> transitions) {
        int size = transitions.size();
        for (int i = 0; i < size; i++) {
            if (!impl.canHandle(transitions.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static Object getSharedElementTransition(FragmentTransitionImpl impl, Fragment inFragment, Fragment outFragment, boolean isPop) {
        Object sharedElementEnterTransition;
        if (inFragment == null || outFragment == null) {
            return null;
        }
        if (isPop) {
            sharedElementEnterTransition = outFragment.getSharedElementReturnTransition();
        } else {
            sharedElementEnterTransition = inFragment.getSharedElementEnterTransition();
        }
        return impl.wrapTransitionInSet(impl.cloneTransition(sharedElementEnterTransition));
    }

    private static Object getEnterTransition(FragmentTransitionImpl impl, Fragment inFragment, boolean isPop) {
        Object enterTransition;
        if (inFragment == null) {
            return null;
        }
        if (isPop) {
            enterTransition = inFragment.getReenterTransition();
        } else {
            enterTransition = inFragment.getEnterTransition();
        }
        return impl.cloneTransition(enterTransition);
    }

    private static Object getExitTransition(FragmentTransitionImpl impl, Fragment outFragment, boolean isPop) {
        Object exitTransition;
        if (outFragment == null) {
            return null;
        }
        if (isPop) {
            exitTransition = outFragment.getReturnTransition();
        } else {
            exitTransition = outFragment.getExitTransition();
        }
        return impl.cloneTransition(exitTransition);
    }

    private static Object configureSharedElementsReordered(FragmentTransitionImpl impl, ViewGroup sceneRoot, View nonExistentView, ArrayMap<String, String> nameOverrides, FragmentContainerTransition fragments, ArrayList<View> sharedElementsOut, ArrayList<View> sharedElementsIn, Object enterTransition, Object exitTransition) {
        Object sharedElementTransition;
        final Rect epicenter;
        final View epicenterView;
        final Fragment inFragment = fragments.lastIn;
        final Fragment outFragment = fragments.firstOut;
        if (inFragment != null) {
            inFragment.getView().setVisibility(0);
        }
        if (inFragment == null || outFragment == null) {
            return null;
        }
        final boolean inIsPop = fragments.lastInIsPop;
        if (nameOverrides.isEmpty()) {
            sharedElementTransition = null;
        } else {
            sharedElementTransition = getSharedElementTransition(impl, inFragment, outFragment, inIsPop);
        }
        ArrayMap<String, View> outSharedElements = captureOutSharedElements(impl, nameOverrides, sharedElementTransition, fragments);
        final ArrayMap<String, View> inSharedElements = captureInSharedElements(impl, nameOverrides, sharedElementTransition, fragments);
        if (nameOverrides.isEmpty()) {
            sharedElementTransition = null;
            if (outSharedElements != null) {
                outSharedElements.clear();
            }
            if (inSharedElements != null) {
                inSharedElements.clear();
            }
        } else {
            addSharedElementsWithMatchingNames(sharedElementsOut, outSharedElements, nameOverrides.keySet());
            addSharedElementsWithMatchingNames(sharedElementsIn, inSharedElements, nameOverrides.values());
        }
        if (enterTransition == null && exitTransition == null && sharedElementTransition == null) {
            return null;
        }
        callSharedElementStartEnd(inFragment, outFragment, inIsPop, outSharedElements, true);
        if (sharedElementTransition != null) {
            sharedElementsIn.add(nonExistentView);
            impl.setSharedElementTargets(sharedElementTransition, nonExistentView, sharedElementsOut);
            setOutEpicenter(impl, sharedElementTransition, exitTransition, outSharedElements, fragments.firstOutIsPop, fragments.firstOutTransaction);
            epicenter = new Rect();
            epicenterView = getInEpicenterView(inSharedElements, fragments, enterTransition, inIsPop);
            if (epicenterView != null) {
                impl.setEpicenter(enterTransition, epicenter);
            }
        } else {
            epicenter = null;
            epicenterView = null;
        }
        final FragmentTransitionImpl fragmentTransitionImpl = impl;
        OneShotPreDrawListener.add(sceneRoot, new Runnable() {
            public void run() {
                FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, inSharedElements, false);
                if (epicenterView != null) {
                    fragmentTransitionImpl.getBoundsOnScreen(epicenterView, epicenter);
                }
            }
        });
        return sharedElementTransition;
    }

    private static void addSharedElementsWithMatchingNames(ArrayList<View> views, ArrayMap<String, View> sharedElements, Collection<String> nameOverridesSet) {
        for (int i = sharedElements.size() - 1; i >= 0; i--) {
            View view = sharedElements.valueAt(i);
            if (nameOverridesSet.contains(ViewCompat.getTransitionName(view))) {
                views.add(view);
            }
        }
    }

    private static Object configureSharedElementsOrdered(FragmentTransitionImpl impl, ViewGroup sceneRoot, View nonExistentView, ArrayMap<String, String> nameOverrides, FragmentContainerTransition fragments, ArrayList<View> sharedElementsOut, ArrayList<View> sharedElementsIn, Object enterTransition, Object exitTransition) {
        Object sharedElementTransition;
        Object sharedElementTransition2;
        final Rect inEpicenter;
        final Fragment inFragment = fragments.lastIn;
        final Fragment outFragment = fragments.firstOut;
        if (inFragment == null || outFragment == null) {
            return null;
        }
        final boolean inIsPop = fragments.lastInIsPop;
        if (nameOverrides.isEmpty()) {
            sharedElementTransition = null;
        } else {
            sharedElementTransition = getSharedElementTransition(impl, inFragment, outFragment, inIsPop);
        }
        ArrayMap<String, View> outSharedElements = captureOutSharedElements(impl, nameOverrides, sharedElementTransition, fragments);
        if (nameOverrides.isEmpty()) {
            sharedElementTransition2 = null;
        } else {
            sharedElementsOut.addAll(outSharedElements.values());
            sharedElementTransition2 = sharedElementTransition;
        }
        if (enterTransition == null && exitTransition == null && sharedElementTransition2 == null) {
            return null;
        }
        callSharedElementStartEnd(inFragment, outFragment, inIsPop, outSharedElements, true);
        if (sharedElementTransition2 != null) {
            inEpicenter = new Rect();
            impl.setSharedElementTargets(sharedElementTransition2, nonExistentView, sharedElementsOut);
            setOutEpicenter(impl, sharedElementTransition2, exitTransition, outSharedElements, fragments.firstOutIsPop, fragments.firstOutTransaction);
            if (enterTransition != null) {
                impl.setEpicenter(enterTransition, inEpicenter);
            }
        } else {
            inEpicenter = null;
        }
        final FragmentTransitionImpl fragmentTransitionImpl = impl;
        final ArrayMap<String, String> arrayMap = nameOverrides;
        final Object obj = sharedElementTransition2;
        final FragmentContainerTransition fragmentContainerTransition = fragments;
        final ArrayList<View> arrayList = sharedElementsIn;
        final View view = nonExistentView;
        final ArrayList<View> arrayList2 = sharedElementsOut;
        final Object obj2 = enterTransition;
        OneShotPreDrawListener.add(sceneRoot, new Runnable() {
            public void run() {
                ArrayMap<String, View> inSharedElements = FragmentTransition.captureInSharedElements(fragmentTransitionImpl, arrayMap, obj, fragmentContainerTransition);
                if (inSharedElements != null) {
                    arrayList.addAll(inSharedElements.values());
                    arrayList.add(view);
                }
                FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, inSharedElements, false);
                if (obj != null) {
                    fragmentTransitionImpl.swapSharedElementTargets(obj, arrayList2, arrayList);
                    View inEpicenterView = FragmentTransition.getInEpicenterView(inSharedElements, fragmentContainerTransition, obj2, inIsPop);
                    if (inEpicenterView != null) {
                        fragmentTransitionImpl.getBoundsOnScreen(inEpicenterView, inEpicenter);
                    }
                }
            }
        });
        return sharedElementTransition2;
    }

    private static ArrayMap<String, View> captureOutSharedElements(FragmentTransitionImpl impl, ArrayMap<String, String> nameOverrides, Object sharedElementTransition, FragmentContainerTransition fragments) {
        SharedElementCallback sharedElementCallback;
        ArrayList<String> names;
        if (nameOverrides.isEmpty() || sharedElementTransition == null) {
            nameOverrides.clear();
            return null;
        }
        Fragment outFragment = fragments.firstOut;
        ArrayMap<String, View> outSharedElements = new ArrayMap<>();
        impl.findNamedViews(outSharedElements, outFragment.getView());
        BackStackRecord outTransaction = fragments.firstOutTransaction;
        if (fragments.firstOutIsPop) {
            sharedElementCallback = outFragment.getEnterTransitionCallback();
            names = outTransaction.mSharedElementTargetNames;
        } else {
            sharedElementCallback = outFragment.getExitTransitionCallback();
            names = outTransaction.mSharedElementSourceNames;
        }
        outSharedElements.retainAll(names);
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(names, outSharedElements);
            for (int i = names.size() - 1; i >= 0; i--) {
                String name = names.get(i);
                View view = outSharedElements.get(name);
                if (view == null) {
                    nameOverrides.remove(name);
                } else if (!name.equals(ViewCompat.getTransitionName(view))) {
                    nameOverrides.put(ViewCompat.getTransitionName(view), nameOverrides.remove(name));
                }
            }
            return outSharedElements;
        }
        nameOverrides.retainAll(outSharedElements.keySet());
        return outSharedElements;
    }

    static ArrayMap<String, View> captureInSharedElements(FragmentTransitionImpl impl, ArrayMap<String, String> nameOverrides, Object sharedElementTransition, FragmentContainerTransition fragments) {
        SharedElementCallback sharedElementCallback;
        ArrayList<String> names;
        String key;
        Fragment inFragment = fragments.lastIn;
        View fragmentView = inFragment.getView();
        if (nameOverrides.isEmpty() || sharedElementTransition == null || fragmentView == null) {
            nameOverrides.clear();
            return null;
        }
        ArrayMap<String, View> inSharedElements = new ArrayMap<>();
        impl.findNamedViews(inSharedElements, fragmentView);
        BackStackRecord inTransaction = fragments.lastInTransaction;
        if (fragments.lastInIsPop) {
            sharedElementCallback = inFragment.getExitTransitionCallback();
            names = inTransaction.mSharedElementSourceNames;
        } else {
            sharedElementCallback = inFragment.getEnterTransitionCallback();
            names = inTransaction.mSharedElementTargetNames;
        }
        if (names != null) {
            inSharedElements.retainAll(names);
            inSharedElements.retainAll(nameOverrides.values());
        }
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(names, inSharedElements);
            for (int i = names.size() - 1; i >= 0; i--) {
                String name = names.get(i);
                View view = inSharedElements.get(name);
                if (view == null) {
                    String key2 = findKeyForValue(nameOverrides, name);
                    if (key2 != null) {
                        nameOverrides.remove(key2);
                    }
                } else if (!name.equals(ViewCompat.getTransitionName(view)) && (key = findKeyForValue(nameOverrides, name)) != null) {
                    nameOverrides.put(key, ViewCompat.getTransitionName(view));
                }
            }
            return inSharedElements;
        }
        retainValues(nameOverrides, inSharedElements);
        return inSharedElements;
    }

    private static String findKeyForValue(ArrayMap<String, String> map, String value) {
        int numElements = map.size();
        for (int i = 0; i < numElements; i++) {
            if (value.equals(map.valueAt(i))) {
                return map.keyAt(i);
            }
        }
        return null;
    }

    static View getInEpicenterView(ArrayMap<String, View> inSharedElements, FragmentContainerTransition fragments, Object enterTransition, boolean inIsPop) {
        String targetName;
        BackStackRecord inTransaction = fragments.lastInTransaction;
        if (enterTransition == null || inSharedElements == null || inTransaction.mSharedElementSourceNames == null || inTransaction.mSharedElementSourceNames.isEmpty()) {
            return null;
        }
        if (inIsPop) {
            targetName = inTransaction.mSharedElementSourceNames.get(0);
        } else {
            targetName = inTransaction.mSharedElementTargetNames.get(0);
        }
        return inSharedElements.get(targetName);
    }

    private static void setOutEpicenter(FragmentTransitionImpl impl, Object sharedElementTransition, Object exitTransition, ArrayMap<String, View> outSharedElements, boolean outIsPop, BackStackRecord outTransaction) {
        String sourceName;
        if (outTransaction.mSharedElementSourceNames != null && !outTransaction.mSharedElementSourceNames.isEmpty()) {
            if (outIsPop) {
                sourceName = outTransaction.mSharedElementTargetNames.get(0);
            } else {
                sourceName = outTransaction.mSharedElementSourceNames.get(0);
            }
            View outEpicenterView = outSharedElements.get(sourceName);
            impl.setEpicenter(sharedElementTransition, outEpicenterView);
            if (exitTransition != null) {
                impl.setEpicenter(exitTransition, outEpicenterView);
            }
        }
    }

    private static void retainValues(ArrayMap<String, String> nameOverrides, ArrayMap<String, View> namedViews) {
        for (int i = nameOverrides.size() - 1; i >= 0; i--) {
            if (!namedViews.containsKey(nameOverrides.valueAt(i))) {
                nameOverrides.removeAt(i);
            }
        }
    }

    static void callSharedElementStartEnd(Fragment inFragment, Fragment outFragment, boolean isPop, ArrayMap<String, View> sharedElements, boolean isStart) {
        SharedElementCallback sharedElementCallback;
        if (isPop) {
            sharedElementCallback = outFragment.getEnterTransitionCallback();
        } else {
            sharedElementCallback = inFragment.getEnterTransitionCallback();
        }
        if (sharedElementCallback != null) {
            ArrayList<View> views = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            int count = sharedElements == null ? 0 : sharedElements.size();
            for (int i = 0; i < count; i++) {
                names.add(sharedElements.keyAt(i));
                views.add(sharedElements.valueAt(i));
            }
            if (isStart) {
                sharedElementCallback.onSharedElementStart(names, views, (List<View>) null);
            } else {
                sharedElementCallback.onSharedElementEnd(names, views, (List<View>) null);
            }
        }
    }

    static ArrayList<View> configureEnteringExitingViews(FragmentTransitionImpl impl, Object transition, Fragment fragment, ArrayList<View> sharedElements, View nonExistentView) {
        ArrayList<View> viewList = null;
        if (transition != null) {
            viewList = new ArrayList<>();
            View root = fragment.getView();
            if (root != null) {
                impl.captureTransitioningViews(viewList, root);
            }
            if (sharedElements != null) {
                viewList.removeAll(sharedElements);
            }
            if (!viewList.isEmpty()) {
                viewList.add(nonExistentView);
                impl.addTargets(transition, viewList);
            }
        }
        return viewList;
    }

    static void setViewVisibility(ArrayList<View> views, int visibility) {
        if (views != null) {
            for (int i = views.size() - 1; i >= 0; i--) {
                views.get(i).setVisibility(visibility);
            }
        }
    }

    private static Object mergeTransitions(FragmentTransitionImpl impl, Object enterTransition, Object exitTransition, Object sharedElementTransition, Fragment inFragment, boolean isPop) {
        boolean overlap = true;
        if (!(enterTransition == null || exitTransition == null || inFragment == null)) {
            overlap = isPop ? inFragment.getAllowReturnTransitionOverlap() : inFragment.getAllowEnterTransitionOverlap();
        }
        if (overlap) {
            return impl.mergeTransitionsTogether(exitTransition, enterTransition, sharedElementTransition);
        }
        return impl.mergeTransitionsInSequence(exitTransition, enterTransition, sharedElementTransition);
    }

    public static void calculateFragments(BackStackRecord transaction, SparseArray<FragmentContainerTransition> transitioningFragments, boolean isReordered) {
        int numOps = transaction.mOps.size();
        for (int opNum = 0; opNum < numOps; opNum++) {
            addToFirstInLastOut(transaction, transaction.mOps.get(opNum), transitioningFragments, false, isReordered);
        }
    }

    public static void calculatePopFragments(BackStackRecord transaction, SparseArray<FragmentContainerTransition> transitioningFragments, boolean isReordered) {
        if (transaction.mManager.mContainer.onHasView()) {
            for (int opNum = transaction.mOps.size() - 1; opNum >= 0; opNum--) {
                addToFirstInLastOut(transaction, transaction.mOps.get(opNum), transitioningFragments, true, isReordered);
            }
        }
    }

    static boolean supportsTransition() {
        return (PLATFORM_IMPL == null && SUPPORT_IMPL == null) ? false : true;
    }

    private static void addToFirstInLastOut(BackStackRecord transaction, BackStackRecord.Op op, SparseArray<FragmentContainerTransition> transitioningFragments, boolean isPop, boolean isReorderedTransaction) {
        int containerId;
        boolean setFirstOut;
        boolean setLastIn;
        Fragment fragment = op.fragment;
        if (fragment != null && (containerId = fragment.mContainerId) != 0) {
            boolean setLastIn2 = false;
            boolean wasRemoved = false;
            boolean setFirstOut2 = false;
            boolean wasAdded = false;
            switch (isPop ? INVERSE_OPS[op.cmd] : op.cmd) {
                case 1:
                case 7:
                    if (isReorderedTransaction) {
                        setLastIn = fragment.mIsNewlyAdded;
                    } else {
                        setLastIn = !fragment.mAdded && !fragment.mHidden;
                    }
                    wasAdded = true;
                    break;
                case 3:
                case 6:
                    if (isReorderedTransaction) {
                        setFirstOut = !fragment.mAdded && fragment.mView != null && fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f;
                    } else {
                        setFirstOut = fragment.mAdded && !fragment.mHidden;
                    }
                    wasRemoved = true;
                    break;
                case 4:
                    if (isReorderedTransaction) {
                        setFirstOut2 = fragment.mHiddenChanged && fragment.mAdded && fragment.mHidden;
                    } else {
                        setFirstOut2 = fragment.mAdded && !fragment.mHidden;
                    }
                    wasRemoved = true;
                    break;
                case 5:
                    if (isReorderedTransaction) {
                        setLastIn2 = fragment.mHiddenChanged && !fragment.mHidden && fragment.mAdded;
                    } else {
                        setLastIn2 = fragment.mHidden;
                    }
                    wasAdded = true;
                    break;
            }
            FragmentContainerTransition containerTransition = transitioningFragments.get(containerId);
            if (setLastIn2) {
                containerTransition = ensureContainer(containerTransition, transitioningFragments, containerId);
                containerTransition.lastIn = fragment;
                containerTransition.lastInIsPop = isPop;
                containerTransition.lastInTransaction = transaction;
            }
            if (!isReorderedTransaction && wasAdded) {
                if (containerTransition != null && containerTransition.firstOut == fragment) {
                    containerTransition.firstOut = null;
                }
                FragmentManagerImpl manager = transaction.mManager;
                if (fragment.mState < 1 && manager.mCurState >= 1 && !transaction.mReorderingAllowed) {
                    manager.makeActive(fragment);
                    manager.moveToState(fragment, 1, 0, 0, false);
                }
            }
            if (setFirstOut2 && (containerTransition == null || containerTransition.firstOut == null)) {
                containerTransition = ensureContainer(containerTransition, transitioningFragments, containerId);
                containerTransition.firstOut = fragment;
                containerTransition.firstOutIsPop = isPop;
                containerTransition.firstOutTransaction = transaction;
            }
            if (!isReorderedTransaction && wasRemoved && containerTransition != null && containerTransition.lastIn == fragment) {
                containerTransition.lastIn = null;
            }
        }
    }

    private static FragmentContainerTransition ensureContainer(FragmentContainerTransition containerTransition, SparseArray<FragmentContainerTransition> transitioningFragments, int containerId) {
        if (containerTransition != null) {
            return containerTransition;
        }
        FragmentContainerTransition containerTransition2 = new FragmentContainerTransition();
        transitioningFragments.put(containerId, containerTransition2);
        return containerTransition2;
    }

    static class FragmentContainerTransition {
        public Fragment firstOut;
        public boolean firstOutIsPop;
        public BackStackRecord firstOutTransaction;
        public Fragment lastIn;
        public boolean lastInIsPop;
        public BackStackRecord lastInTransaction;

        FragmentContainerTransition() {
        }
    }

    private FragmentTransition() {
    }
}
