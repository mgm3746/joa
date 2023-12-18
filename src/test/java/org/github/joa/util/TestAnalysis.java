/**********************************************************************************************************************
 * JVM Options Analyzer                                                                                               *
 *                                                                                                                    *
 * Copyright (c) 2022-2023 Mike Millson                                                                                    *
 *                                                                                                                    *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License       *
 * v. 2.0 which is available at https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0 which is    *
 * available at https://www.apache.org/licenses/LICENSE-2.0.                                                          *
 *                                                                                                                    *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0                                                                     *
 *                                                                                                                    *
 * Contributors:                                                                                                      *
 *    Mike Millson - initial API and implementation                                                                   *
 *********************************************************************************************************************/
package org.github.joa.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.github.joa.JvmOptions;
import org.github.joa.domain.Bit;
import org.github.joa.domain.GarbageCollector;
import org.github.joa.domain.JvmContext;
import org.github.joa.domain.Os;
import org.junit.jupiter.api.Test;

public class TestAnalysis {

    @Test
    void testActiveProcessorCount() {
        String opts = "-Xss128k -XX:ActiveProcessorCount=10 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_ACTIVE_PROCESSOR_COUNT.getKey()),
                Analysis.INFO_ACTIVE_PROCESSOR_COUNT + " analysis not identified.");
    }

    @Test
    void testAggressiveOptsEnabled() {
        String opts = "-XX:+AggressiveOpts";
        JvmContext context = new JvmContext(opts);
        context.setContainer(true);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_AGGRESSIVE_OPTS_ENABLED.getKey()),
                Analysis.INFO_AGGRESSIVE_OPTS_ENABLED + " analysis not identified.");
    }

    @Test
    void testAlwaysPreTouchContainer() {
        String opts = "-XX:+AlwaysPreTouch";
        JvmContext context = new JvmContext(opts);
        context.setContainer(true);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_ALWAYS_PRE_TOUCH_CONTAINER.getKey()),
                Analysis.WARN_ALWAYS_PRE_TOUCH_CONTAINER + " analysis not identified.");
    }

    @Test
    void testAttachMechanismDisabled() {
        String opts = "-Xss128k -XX:+DisableAttachMechanism -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_DISABLE_ATTACH_MECHANISM.getKey()),
                Analysis.WARN_DISABLE_ATTACH_MECHANISM + " analysis not identified.");
    }

    @Test
    void testBisasedLockingDisabledJdk17() {
        String opts = "-Xss128k -XX:-UseBiasedLocking -Xms2048M";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(17);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_BIASED_LOCKING_DISABLED_REDUNDANT.getKey()),
                Analysis.INFO_BIASED_LOCKING_DISABLED_REDUNDANT + " analysis not identified.");
    }

    @Test
    void testBisasedLockingDisabledJdk17Shenandoah() {
        String opts = "-XX:+UseShenandoahGC -Xss128k -XX:-UseBiasedLocking -Xms2048M";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(17);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_BIASED_LOCKING_DISABLED.getKey()),
                Analysis.INFO_BIASED_LOCKING_DISABLED + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_BIASED_LOCKING_DISABLED_REDUNDANT.getKey()),
                Analysis.INFO_BIASED_LOCKING_DISABLED_REDUNDANT + " analysis incorrectly identified.");
    }

    @Test
    void testBisasedLockingDisabledJdkUnknown() {
        String opts = "-Xss128k -XX:-UseBiasedLocking -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_BIASED_LOCKING_DISABLED.getKey()),
                Analysis.INFO_BIASED_LOCKING_DISABLED + " analysis not identified.");
    }

    @Test
    void testBisasedLockingEnabledJdk11() {
        String opts = "-Xss128k -XX:+UseBiasedLocking -Xms2048M";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(11);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_BIASED_LOCKING_ENABLED.getKey()),
                Analysis.INFO_BIASED_LOCKING_ENABLED + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_BIASED_LOCKING_ENABLED_REDUNDANT.getKey()),
                Analysis.INFO_BIASED_LOCKING_ENABLED_REDUNDANT + " analysis not identified.");
    }

    @Test
    void testBisasedLockingEnabledJdk17() {
        String opts = "-Xss128k -XX:+UseBiasedLocking -Xms2048M";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(17);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_BIASED_LOCKING_ENABLED_REDUNDANT.getKey()),
                Analysis.INFO_BIASED_LOCKING_ENABLED_REDUNDANT + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_BIASED_LOCKING_ENABLED.getKey()),
                Analysis.INFO_BIASED_LOCKING_ENABLED + " analysis incorrectly identified.");
    }

    @Test
    void testBisasedLockingEnabledShenandoahJdk11Default() {
        String opts = "-Xss128k -Xms2048M";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(11);
        context.getGarbageCollectors().add(GarbageCollector.SHENANDOAH);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_BIASED_LOCKING_ENABLED_REDUNDANT.getKey()),
                Analysis.INFO_BIASED_LOCKING_ENABLED_REDUNDANT + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_BIASED_LOCKING_ENABLED_SHENANDOAH.getKey()),
                Analysis.WARN_BIASED_LOCKING_ENABLED_SHENANDOAH + " analysis not identified.");
        String literal = "It is recommended to disable biased locking when using the Shenandoah collector. Add "
                + "-XX:-UseBiasedLocking to override the JVM default.";
        assertEquals(literal, jvmOptions.getAnalysisLiteral(Analysis.WARN_BIASED_LOCKING_ENABLED_SHENANDOAH.getKey()),
                Analysis.WARN_BIASED_LOCKING_ENABLED_SHENANDOAH + " not correct.");
    }

    @Test
    void testBisasedLockingEnabledShenandoahJdk11Explicit() {
        String opts = "-Xss128k -XX:+UseBiasedLocking -Xms2048M";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(11);
        context.getGarbageCollectors().add(GarbageCollector.SHENANDOAH);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_BIASED_LOCKING_ENABLED_REDUNDANT.getKey()),
                Analysis.INFO_BIASED_LOCKING_ENABLED_REDUNDANT + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_BIASED_LOCKING_ENABLED_SHENANDOAH.getKey()),
                Analysis.WARN_BIASED_LOCKING_ENABLED_SHENANDOAH + " analysis not identified.");
        String literal = "It is recommended to disable biased locking when using the Shenandoah collector. Replace "
                + "-XX:+UseBiasedLocking with -XX:-UseBiasedLocking.";
        assertEquals(literal, jvmOptions.getAnalysisLiteral(Analysis.WARN_BIASED_LOCKING_ENABLED_SHENANDOAH.getKey()),
                Analysis.WARN_BIASED_LOCKING_ENABLED_SHENANDOAH + " not correct.");
    }

    @Test
    void testBisasedLockingEnabledShenandoahJdk17() {
        String opts = "-Xss128k -XX:+UseBiasedLocking -Xms2048M";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(17);
        context.getGarbageCollectors().add(GarbageCollector.SHENANDOAH);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_BIASED_LOCKING_ENABLED_REDUNDANT.getKey()),
                Analysis.INFO_BIASED_LOCKING_ENABLED_REDUNDANT + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_BIASED_LOCKING_ENABLED_SHENANDOAH.getKey()),
                Analysis.WARN_BIASED_LOCKING_ENABLED_SHENANDOAH + " analysis not identified.");
        String literal = "It is recommended to disable biased locking when using the Shenandoah collector. Remove "
                + "-XX:+UseBiasedLocking.";
        assertEquals(literal, jvmOptions.getAnalysisLiteral(Analysis.WARN_BIASED_LOCKING_ENABLED_SHENANDOAH.getKey()),
                Analysis.WARN_BIASED_LOCKING_ENABLED_SHENANDOAH + " not correct.");
    }

    @Test
    void testBytecodeBackgroundCompilationDisabledNonStandard() {
        String opts = "-Xss128k -Xbatch -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_BYTECODE_BACK_COMP_DISABLED.getKey()),
                Analysis.WARN_BYTECODE_BACK_COMP_DISABLED + " analysis not identified.");
    }

    @Test
    void testBytecodeBackgroundCompilationDisabledStandard() {
        String opts = "-Xss128k -XX:-BackgroundCompilation -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_BYTECODE_BACK_COMP_DISABLED.getKey()),
                Analysis.WARN_BYTECODE_BACK_COMP_DISABLED + " analysis not identified.");
    }

    @Test
    void testCGroupMemoryLimit() {
        String opts = "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CGROUP_MEMORY_LIMIT.getKey()),
                Analysis.WARN_CGROUP_MEMORY_LIMIT + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CGROUP_MEMORY_LIMIT_OVERRIDE.getKey()),
                Analysis.WARN_CGROUP_MEMORY_LIMIT_OVERRIDE + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
        String warnExperimental = "Experimental options. Consider removing the following, as they are not "
                + "recommended/supported in production: -XX:+UnlockExperimentalVMOptions "
                + "-XX:+UseCGroupMemoryLimitForHeap.";
        assertEquals(warnExperimental,
                jvmOptions.getAnalysisLiteral(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " not correct.");
    }

    @Test
    void testCGroupMemoryLimitOverrideMaxHeap() {
        String opts = "-XX:MaxHeapSize=2048m -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CGROUP_MEMORY_LIMIT_OVERRIDE.getKey()),
                Analysis.WARN_CGROUP_MEMORY_LIMIT_OVERRIDE + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CGROUP_MEMORY_LIMIT.getKey()),
                Analysis.WARN_CGROUP_MEMORY_LIMIT + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testCGroupMemoryLimitOverrideXmx() {
        String opts = "-Xmx2048m -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CGROUP_MEMORY_LIMIT_OVERRIDE.getKey()),
                Analysis.WARN_CGROUP_MEMORY_LIMIT_OVERRIDE + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CGROUP_MEMORY_LIMIT.getKey()),
                Analysis.WARN_CGROUP_MEMORY_LIMIT + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testCheckJni() {
        String opts = "-Xss128k -Xcheck:jni -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CHECK_JNI_ENABLED.getKey()),
                Analysis.WARN_CHECK_JNI_ENABLED + " analysis not identified.");
    }

    @Test
    void testClassUnloadingDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:-ClassUnloading";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CLASS_UNLOADING_DISABLED.getKey()),
                Analysis.WARN_CLASS_UNLOADING_DISABLED + " analysis not identified.");
    }

    @Test
    void testClassUnloadingDisabledCms() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseConcMarkSweepGC -XX:-CMSClassUnloadingEnabled";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CMS_CLASS_UNLOADING_DISABLED.getKey()),
                Analysis.WARN_CMS_CLASS_UNLOADING_DISABLED + " analysis not identified.");
    }

    @Test
    void testClassUnloadingDisabledCmsDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseConcMarkSweepGC -XX:-CMSClassUnloadingEnabled";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CMS_CLASS_UNLOADING_DISABLED.getKey()),
                Analysis.WARN_CMS_CLASS_UNLOADING_DISABLED + " analysis incorrectly identified.");
    }

    @Test
    void testClassVerificationDisabled() {
        String opts = "-Xss512 -Xmx33g -Xverify:none";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_VERIFY_NONE.getKey()),
                Analysis.WARN_VERIFY_NONE + " analysis not identified.");
    }

    @Test
    void testClientFlag() {
        String opts = "-Xss512 -client -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        // 64-bit is assumed
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_64_CLIENT.getKey()),
                Analysis.INFO_64_CLIENT + " analysis not identified.");
    }

    /**
     * Test if CMS collector disabled with -XX:-UseConcMarkSweepGC -XX:-UseParNewGC.
     */
    @Test
    void testCmsDisabledJdkUnknown() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseParNewGC -XX:-UseConcMarkSweepGC "
                + "-XX:CMSInitiatingOccupancyFraction=70";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_JDK8_CMS_PAR_NEW_DISABLED.getKey()),
                Analysis.ERROR_JDK8_CMS_PAR_NEW_DISABLED + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT.getKey()),
                Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CMS_DISABLED.getKey()),
                Analysis.INFO_CMS_DISABLED + " analysis not identified.");
    }

    @Test
    void testCmsEdenChunksRecordAlways() {
        String opts = "-Xss128k -XX:+CMSEdenChunksRecordAlways -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CMS_EDEN_CHUNK_RECORD_ALWAYS.getKey()),
                Analysis.INFO_CMS_EDEN_CHUNK_RECORD_ALWAYS + " analysis not identified.");
    }

    @Test
    void testCmsIncrementalMode() {
        String opts = "-Xss128k -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CMS_INCREMENTAL_MODE.getKey()),
                Analysis.INFO_CMS_INCREMENTAL_MODE + " analysis not identified.");
    }

    @Test
    void testCmsIncrementalModeWithInitatingOccupancyFractionCms() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode "
                + "-XX:CMSInitiatingOccupancyFraction=70";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CMS_INC_MODE_WITH_INIT_OCCUP_FRACT.getKey()),
                Analysis.WARN_CMS_INC_MODE_WITH_INIT_OCCUP_FRACT + " analysis not identified.");

    }

    @Test
    void testCmsIncrementalModeWithInitatingOccupancyFractionCmsDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseConcMarkSweepGC -XX:+CMSIncrementalMode "
                + "-XX:CMSInitiatingOccupancyFraction=70";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CMS_INC_MODE_WITH_INIT_OCCUP_FRACT.getKey()),
                Analysis.WARN_CMS_INC_MODE_WITH_INIT_OCCUP_FRACT + " analysis incorrectly identified.");
    }

    /**
     * Test if PAR_NEW collector is enabled/disabled when the CMS collector is not used.
     */
    @Test
    void testCmsParNewCruftJdkUnknown() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseParNewGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_JDK8_CMS_PAR_NEW_DISABLED.getKey()),
                Analysis.ERROR_JDK8_CMS_PAR_NEW_DISABLED + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_CMS_DISABLED.getKey()),
                Analysis.INFO_CMS_DISABLED + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT.getKey()),
                Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT + " analysis not identified.");
    }

    /**
     * Test if PAR_NEW collector is enabled/disabled when the CMS collector is not used.
     */
    @Test
    void testCmsParNewCruftNone() {
        String opts = "-Xss128k -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_JDK8_CMS_PAR_NEW_DISABLED.getKey()),
                Analysis.ERROR_JDK8_CMS_PAR_NEW_DISABLED + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_CMS_DISABLED.getKey()),
                Analysis.INFO_CMS_DISABLED + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT.getKey()),
                Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT + " analysis incorrectly identified.");
    }

    @Test
    void testCmsWaitDuration() {
        String opts = "-Xss128k -XX:CMSWaitDuration=10000 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CMS_WAIT_DURATION.getKey()),
                Analysis.INFO_CMS_WAIT_DURATION + " analysis not identified.");
    }

    @Test
    void testCmsYoungCmsOld() {
        String opts = "-Xss128k -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xms2048M";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_CMS_MISSING.getKey()),
                Analysis.ERROR_CMS_MISSING + " analysis incorrectly identified.");
    }

    @Test
    void testCmsYoungSerialOld() {
        String opts = "-Xss128k -XX:+UseParNewGC -Xms2048M";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_CMS_MISSING.getKey()),
                Analysis.ERROR_CMS_MISSING + " analysis not identified.");
    }

    @Test
    void testCollectorSingleCpu() {
        String opts = "-XX:GCLogFileSize=3145728 -XX:InitialHeapSize=134217728 -XX:MaxHeapSize=6291456000 "
                + "-XX:NumberOfGCLogFiles=5 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails "
                + "-XX:+PrintGCTimeStamps -XX:-TraceClassUnloading -XX:+UseCompressedClassPointers "
                + "-XX:+UseCompressedOops -XX:+UseGCLogFileRotation";
        JvmContext context = new JvmContext(opts);
        context.getGarbageCollectors().add(GarbageCollector.SERIAL_NEW);
        context.getGarbageCollectors().add(GarbageCollector.SERIAL_OLD);
        context.setVersionMajor(8);
        context.setVersionMinor(381);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_GC_SERIAL_ELECTED.getKey()),
                Analysis.INFO_GC_SERIAL_ELECTED + " analysis not identified.");
    }

    /**
     * Test analysis just in time (JIT) compiler disabled.
     */
    @Test
    void testCompilationDisabled() {
        String opts = "-Xss128k -Xint -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_BYTECODE_COMPILE_DISABLED.getKey()),
                Analysis.WARN_BYTECODE_COMPILE_DISABLED + " analysis not identified.");
    }

    /**
     * Test analysis compilation on first invocation enabled.
     */
    @Test
    void testCompilationOnFirstInvocation() {
        String opts = "-Xss128k -Xcomp -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_BYTECODE_COMPILE_FIRST_INVOCATION.getKey()),
                Analysis.WARN_BYTECODE_COMPILE_FIRST_INVOCATION + " analysis not identified.");
    }

    @Test
    void testCompilerThreads() {
        String opts = "-Xss128k -XX:CICompilerCount=2 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CI_COMPILER_COUNT.getKey()),
                Analysis.INFO_CI_COMPILER_COUNT + " analysis not identified.");
    }

    @Test
    void testCompileThresholdIgnored() {
        String opts = "-Xss512 -XX:CompileThreshold=200 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_COMPILE_THRESHOLD_IGNORED.getKey()),
                Analysis.INFO_COMPILE_THRESHOLD_IGNORED + " analysis not identified.");
    }

    @Test
    void testCompressedClassPointersDisabledHeapLt32G() {
        String opts = "-Xss128k -XX:-UseCompressedClassPointers -XX:+UseCompressedOops -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_CLASS_DISABLED_HEAP_LT_32G.getKey()),
                Analysis.WARN_COMP_CLASS_DISABLED_HEAP_LT_32G + " analysis not identified.");
    }

    @Test
    void testCompressedClassPointersDisabledHeapUnknown() {
        String opts = "-Xss128k -XX:-UseCompressedClassPointers -XX:+UseCompressedOops";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_CLASS_DISABLED_HEAP_UNK.getKey()),
                Analysis.WARN_COMP_CLASS_DISABLED_HEAP_UNK + " analysis not identified.");
    }

    @Test
    void testCompressedClassPointersEnabledCompressedOopsDisabledHeapUnknown() {
        String opts = "-Xss128k -XX:-UseCompressedOops -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_DISABLED_HEAP_UNK.getKey()),
                Analysis.WARN_COMP_OOPS_DISABLED_HEAP_UNK + " analysis not identified.");
    }

    @Test
    void testCompressedClassPointersEnabledHeapGt32G() {
        String opts = "-Xss128k -XX:+UseCompressedClassPointers -Xmx32g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_CLASS_ENABLED_HEAP_GT_32G.getKey()),
                Analysis.WARN_COMP_CLASS_ENABLED_HEAP_GT_32G + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_ENABLED_HEAP_GT_32G.getKey()),
                Analysis.WARN_COMP_OOPS_ENABLED_HEAP_GT_32G + " incorrectly identified.");
    }

    @Test
    void testCompressedClassSpaceSizeWithCompressedClassPointersDisabledHeapUnknown() {
        String opts = "-Xss128k -XX:CompressedClassSpaceSize=1G -XX:-UseCompressedClassPointers -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_CLASS_DISABLED_HEAP_UNK.getKey()),
                Analysis.WARN_COMP_CLASS_DISABLED_HEAP_UNK + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_COMP_CLASS_SIZE_COMP_CLASS_DISABLED.getKey()),
                Analysis.INFO_COMP_CLASS_SIZE_COMP_CLASS_DISABLED + " analysis not identified.");
    }

    @Test
    void testCompressedClassSpaceSizeWithCompressedOopsDisabledHeapUnknown() {
        String opts = "-Xss128k -XX:CompressedClassSpaceSize=1G -XX:-UseCompressedOops -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_DISABLED_HEAP_UNK.getKey()),
                Analysis.WARN_COMP_OOPS_DISABLED_HEAP_UNK + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_COMP_CLASS_SIZE_COMP_OOPS_DISABLED.getKey()),
                Analysis.INFO_COMP_CLASS_SIZE_COMP_OOPS_DISABLED + " analysis not identified.");
    }

    @Test
    void testCompressedOopsDisabledHeapEqual32G() {
        String opts = "-Xss128k -XX:-UseCompressedOops -Xmx32G";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_DISABLED_HEAP_LT_32G.getKey()),
                Analysis.WARN_COMP_OOPS_DISABLED_HEAP_LT_32G + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_DISABLED_HEAP_UNK.getKey()),
                Analysis.WARN_COMP_OOPS_DISABLED_HEAP_UNK + " analysis incorrectly identified.");
    }

    @Test
    void testCompressedOopsDisabledHeapGreater32G() {
        String opts = "-Xss128k -XX:-UseCompressedOops -Xmx40G";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_DISABLED_HEAP_LT_32G.getKey()),
                Analysis.WARN_COMP_OOPS_DISABLED_HEAP_LT_32G + " analysis incorrectly identified.");
    }

    @Test
    void testCompressedOopsDisabledHeapLess32G() {
        String opts = "-Xss128k -XX:-UseCompressedOops -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_DISABLED_HEAP_LT_32G.getKey()),
                Analysis.WARN_COMP_OOPS_DISABLED_HEAP_LT_32G + " analysis not identified.");
    }

    @Test
    void testCompressedOopsEnabledHeapGreater32G() {
        String opts = "-Xss128k -XX:+UseCompressedOops -Xmx40G";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_COMP_OOPS_ENABLED_HEAP_GT_32G.getKey()),
                Analysis.WARN_COMP_OOPS_ENABLED_HEAP_GT_32G + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_COMP_CLASS_ENABLED_HEAP_GT_32G.getKey()),
                Analysis.WARN_COMP_CLASS_ENABLED_HEAP_GT_32G + " incorrectly identified.");
    }

    @Test
    void testConcMarkSweepParNewCmsSerialOldIgnored() {
        String opts = "-Xss128k -Xmx2048M  -XX:+ExplicitGCInvokesConcurrent -XX:+UseConcMarkSweepGC -XX:+UseParNewGC "
                + "-XX:-UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        context.getGarbageCollectors().add(GarbageCollector.PAR_NEW);
        context.getGarbageCollectors().add(GarbageCollector.CMS);
        context.getGarbageCollectors().add(GarbageCollector.SERIAL_OLD);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_CMS_MISSING.getKey()),
                Analysis.ERROR_CMS_MISSING + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD.getKey()),
                Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_PAR_NEW_SERIAL_OLD.getKey()),
                Analysis.ERROR_PAR_NEW_SERIAL_OLD + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT.getKey()),
                Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_GC_SERIAL_ELECTED.getKey()),
                Analysis.INFO_GC_SERIAL_ELECTED + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_CRUFT.getKey()),
                Analysis.INFO_PARALLEL_OLD_CRUFT + " analysis not identified.");
        assertNull(jvmOptions.getDuplicates(), "Duplicate options incorrectly identified.");
    }

    @Test
    void testConcurrentio() {
        String opts = "-Xss128k -Xconcurrentio -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CONCURRENTIO.getKey()),
                Analysis.WARN_CONCURRENTIO + " analysis not identified.");
    }

    @Test
    void testContainerSupportDisabled() {
        String opts = "-Xss128k -XX:-UseContainerSupport -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CONTAINER_SUPPORT_DISABLED.getKey()),
                Analysis.WARN_CONTAINER_SUPPORT_DISABLED + " analysis not identified.");
    }

    @Test
    void testD64Flag() {
        String opts = "-Xss512 -d64 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        // 64-bit is assumed
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_64_D64_REDUNDANT.getKey()),
                Analysis.INFO_64_D64_REDUNDANT + " analysis not identified.");
    }

    @Test
    void testDebug() {
        String opts = "-Xdebug -XX:+PrintHeapAtGC -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_DEBUG.getKey()),
                Analysis.INFO_DEBUG + " analysis not identified.");
    }

    /**
     * Test analysis huge DGC intervals.
     */
    @Test
    void testDgcHugeIntervals() {
        String opts = "-Dsun.rmi.dgc.client.gcInteraval=9223372036854775807 "
                + "-Dsun.rmi.dgc.server.gcInterval=9223372036854775807";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL.getKey()),
                Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL.getKey()),
                Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_LARGE.getKey()),
                Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_LARGE + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_LARGE.getKey()),
                Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_LARGE + " analysis nto identified.");
    }

    /**
     * Test analysis not small DGC intervals.
     */
    @Test
    void testDgcNotSmallIntervals() {
        String opts = "-Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL.getKey()),
                Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL.getKey()),
                Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL + " analysis incorrectly identified.");
    }

    /**
     * Test DGC redundant options analysis.
     */
    @Test
    void testDgcRedundantOptions() {
        String opts = "-XX:+DisableExplicitGC -Dsun.rmi.dgc.client.gcInterval=14400000 "
                + "-Dsun.rmi.dgc.server.gcInterval=24400000";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_RMI_DGC_CLIENT_GCINTERVAL_REDUNDANT.getKey()),
                Analysis.INFO_RMI_DGC_CLIENT_GCINTERVAL_REDUNDANT + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_RMI_DGC_SERVER_GCINTERVAL_REDUNDANT.getKey()),
                Analysis.INFO_RMI_DGC_SERVER_GCINTERVAL_REDUNDANT + " analysis not identified.");
    }

    /**
     * Test analysis small DGC intervals
     */
    @Test
    void testDgcSmallIntervals() {
        String opts = "-Dsun.rmi.dgc.client.gcInterval=3599999 -Dsun.rmi.dgc.server.gcInterval=3599999";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL.getKey()),
                Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL.getKey()),
                Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL + " analysis not identified.");
    }

    /**
     * Test analysis small DGC intervals with explicit gc disable
     */
    @Test
    void testDgcSmallIntervalsDisableExplicitGc() {
        String opts = "-Dsun.rmi.dgc.client.gcInterval=3599999 -Dsun.rmi.dgc.server.gcInterval=3599999 "
                + "-XX:+DisableExplicitGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL.getKey()),
                Analysis.WARN_RMI_DGC_CLIENT_GCINTERVAL_SMALL + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL.getKey()),
                Analysis.WARN_RMI_DGC_SERVER_GCINTERVAL_SMALL + " analysis incorrectly identified.");
    }

    /**
     * Test DisableExplicitGC in combination with ExplicitGCInvokesConcurrent.
     */
    @Test
    void testDisableExplictGc() {
        String opts = "-Xss128k -XX:+DisableExplicitGC -XX:+ExplicitGCInvokesConcurrent -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPLICIT_GC_DISABLED.getKey()),
                Analysis.WARN_EXPLICIT_GC_DISABLED + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_EXPLICIT_GC_DISABLED_CONCURRENT.getKey()),
                Analysis.ERROR_EXPLICIT_GC_DISABLED_CONCURRENT + " analysis not identified.");
    }

    @Test
    void testDuplicateAnalysis() {
        JvmContext context = new JvmContext(null);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.addAnalysis(Analysis.INFO_64_CLIENT);
        jvmOptions.addAnalysis(Analysis.INFO_64_CLIENT);
        jvmOptions.addAnalysis(Analysis.INFO_64_CLIENT);
        assertEquals(1, jvmOptions.getAnalysis().size(), "Duplicate analysis.");

    }

    @Test
    void testDups() {
        String opts = "-Xss128k -Xss256k -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_DUPS.getKey()),
                Analysis.ERROR_DUPS + " analysis not identified.");
    }

    @Test
    void testEliminateLocks() {
        String opts = "-Xss512 -Xmx33g -XX:+EliminateLocks";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_ELIMINATE_LOCKS_ENABLED.getKey()),
                Analysis.INFO_ELIMINATE_LOCKS_ENABLED + " analysis not identified.");
    }

    @Test
    void testExplicitGCInvokesConcurrentAndUnloadsClassesDisabled() {
        String opts = "-Xss128k -XX:+DisableExplicitGC -XX:-ExplicitGCInvokesConcurrentAndUnloadsClasses";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CRUFT_EXP_GC_INV_CON_AND_UNL_CLA.getKey()),
                Analysis.INFO_CRUFT_EXP_GC_INV_CON_AND_UNL_CLA + " analysis not identified.");
    }

    @Test
    void testFlightRecorderDisabled() {
        String opts = "-Xss128k -XX:-FlightRecorder -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JFR_FLIGHT_RECORDER_DISABLED.getKey()),
                Analysis.INFO_JFR_FLIGHT_RECORDER_DISABLED + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JFR.getKey()),
                Analysis.INFO_JFR + " analysis incorrectly identified.");
    }

    @Test
    void testFlightRecorderEnabled() {
        String opts = "-Xss128k -XX:+FlightRecorder -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JFR_FLIGHT_RECORDER_ENABLED.getKey()),
                Analysis.INFO_JFR_FLIGHT_RECORDER_ENABLED + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JFR.getKey()),
                Analysis.INFO_JFR + " analysis incorrectly identified.");
    }

    /**
     * Test if explicit not GC handled concurrently.
     */
    @Test
    void testG1MixedGCLiveThresholdPercent() {
        String opts = "-Xss128k -Xms2048M -XX:+UseG1GC -XX:G1MixedGCLiveThresholdPercent=50";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_G1_MIXED_GC_LIVE_THRSHOLD_PRCNT.getKey()),
                Analysis.WARN_G1_MIXED_GC_LIVE_THRSHOLD_PRCNT + " analysis not identified.");
    }

    @Test
    void testGcLoggingToStdout() {
        String opts = "-XX:+PrintGC -XX:+PrintGCCause -XX:+PrintGCDateStamps -XX:+PrintGCDetails "
                + "-XX:+PrintGCTimeStamps";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_GC_LOG_FILE_ROTATION_NOT_ENABLED.getKey()),
                Analysis.WARN_JDK8_GC_LOG_FILE_ROTATION_NOT_ENABLED + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_GC_LOG_STDOUT.getKey()),
                Analysis.INFO_GC_LOG_STDOUT + " analysis not identified.");
    }

    @Test
    void testGcOverheadLimitDisabled() {
        String opts = "-Xss128k -XX:-UseGCOverheadLimit -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_GC_OVERHEAD_LIMIT_DISABLED.getKey()),
                Analysis.INFO_GC_OVERHEAD_LIMIT_DISABLED + " analysis not identified.");
        assertNull(jvmOptions.getUnaccountedDisabledOptions(),
                "-XX:-UseGCOverheadLimit incorrectly identified as an unaccounted disabled option.");
    }

    @Test
    void testGuaranteedSafepointInterval() {
        String opts = "-Xss128k -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=100000 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED.getKey()),
                Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_DIAGNOSTICS_GUARANTEED_SAFEPOINT_INTERVAL.getKey()),
                Analysis.WARN_DIAGNOSTICS_GUARANTEED_SAFEPOINT_INTERVAL + " analysis not identified.");
        String diagnostic = "Diagnostic options. The following should be removed when relevant troubleshooting is "
                + "completed, as they add additional overhead and are not recommended/supported for general production "
                + "use: -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=100000.";
        assertEquals(diagnostic, jvmOptions.getAnalysisLiteral(Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED.getKey()),
                Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED + " not correct.");
    }

    @Test
    void testHeapDumpOnOutOfMemoryErrorDisabled() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:-HeapDumpOnOutOfMemoryError";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_HEAP_DUMP_ON_OOME_DISABLED.getKey()),
                Analysis.WARN_HEAP_DUMP_ON_OOME_DISABLED + " analysis not identified.");
    }

    @Test
    void testHeapDumpOnOutOfMemoryErrorMissing() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_HEAP_DUMP_ON_OOME_MISSING.getKey()),
                Analysis.INFO_HEAP_DUMP_ON_OOME_MISSING + " analysis not identified.");
    }

    @Test
    void testHeapDumpOnOutOfMemoryErrorPathIsDirectory() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:+HeapDumpOnOutOfMemoryError "
                + "-XX:HeapDumpPath=/path/to/";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_HEAP_DUMP_PATH_FILENAME.getKey()),
                Analysis.WARN_HEAP_DUMP_PATH_FILENAME + " analysis incorrectly identified.");
    }

    @Test
    void testHeapDumpOnOutOfMemoryErrorPathIsFileName() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:+HeapDumpOnOutOfMemoryError "
                + "-XX:HeapDumpPath=/path/to/heap.hprof";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_HEAP_DUMP_PATH_FILENAME.getKey()),
                Analysis.WARN_HEAP_DUMP_PATH_FILENAME + " analysis not identified.");
    }

    @Test
    void testHeapDumpOnOutOfMemoryErrorPathIsFileNameNoExtension() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:+HeapDumpOnOutOfMemoryError "
                + "-XX:HeapDumpPath=/path/to";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_HEAP_DUMP_PATH_FILENAME.getKey()),
                Analysis.WARN_HEAP_DUMP_PATH_FILENAME + " analysis not identified.");
    }

    @Test
    void testHeapDumpOnOutOfMemoryErrorPathMissing() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:+HeapDumpOnOutOfMemoryError";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_HEAP_DUMP_PATH_MISSING.getKey()),
                Analysis.INFO_HEAP_DUMP_PATH_MISSING + " analysis not identified.");
    }

    @Test
    void testIgnoreUnrecognizedVmOptions() {
        String opts = "-Xss128k -XX:+IgnoreUnrecognizedVMOptions -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_IGNORE_UNRECOGNIZED_VM_OPTIONS.getKey()),
                Analysis.INFO_IGNORE_UNRECOGNIZED_VM_OPTIONS + " analysis not identified.");
    }

    @Test
    void testInitatingOccupancyOnlyMissingCms() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_CMS_INIT_OCCUPANCY_ONLY_MISSING.getKey()),
                Analysis.INFO_CMS_INIT_OCCUPANCY_ONLY_MISSING + " analysis not identified.");
    }

    @Test
    void testInitatingOccupancyOnlyMissingCmsDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_CMS_INIT_OCCUPANCY_ONLY_MISSING.getKey()),
                Analysis.INFO_CMS_INIT_OCCUPANCY_ONLY_MISSING + " analysis incorrectly identified.");
    }

    /**
     * Test if explicit not GC handled concurrently.
     */
    @Test
    void testInitialNotEqualMaxHeap() {
        String opts = "-Xss128k -Xms2048M -Xmx4096M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_HEAP_MIN_NOT_EQUAL_MAX.getKey()),
                Analysis.INFO_HEAP_MIN_NOT_EQUAL_MAX + " analysis not identified.");
    }

    @Test
    void testInstrumentation() {
        String opts = "-Xss128k -javaagent:/path/to/appdynamics/javaagent.jar -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_INSTRUMENTATION.getKey()),
                Analysis.INFO_INSTRUMENTATION + " analysis not identified.");
    }

    @Test
    void testJdk11GcLogFileOverwrite() {
        String opts = "-Xlog:gc*,safepoint=info:file=gc.log:uptimemillis:filecount=0,filesize=50M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK11_GC_LOG_FILE_OVERWRITE.getKey()),
                Analysis.WARN_JDK11_GC_LOG_FILE_OVERWRITE + " analysis not identified.");
    }

    @Test
    void testJdk11PrintGCDetailsMissingLog() {
        String opts = "-Xss128k -Xlog:gc*,safepoint=info:file=gc_%p_%t.log:time:filecount=4,filesize=50M -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_PRINT_GC_DETAILS_MISSING.getKey()),
                Analysis.WARN_JDK8_PRINT_GC_DETAILS_MISSING + " analysis incorrectly identified.");
    }

    @Test
    void testJdk8G1PriorUpdate40() {
        String opts = "MGM";
        JvmContext context = new JvmContext(opts);
        List<GarbageCollector> collectors = new ArrayList<GarbageCollector>();
        collectors.add(GarbageCollector.G1);
        context.setGarbageCollectors(collectors);
        context.setVersionMajor(8);
        context.setVersionMinor(20);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_G1_PRIOR_U40.getKey()),
                Analysis.WARN_JDK8_G1_PRIOR_U40 + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_G1_PRIOR_U40_RECS.getKey()),
                Analysis.WARN_JDK8_G1_PRIOR_U40_RECS + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis incorrectly identified.");
    }

    @Test
    void testJdk8G1PriorUpdate40WithRecommendedJvmOptions() {
        String opts = "-XX:+UnlockExperimentalVMOptions -XX:G1MixedGCLiveThresholdPercent=85 "
                + "-XX:G1HeapWastePercent=5";
        JvmContext context = new JvmContext(opts);
        List<GarbageCollector> collectors = new ArrayList<GarbageCollector>();
        collectors.add(GarbageCollector.G1);
        context.setGarbageCollectors(collectors);
        JvmOptions jvmOptions = new JvmOptions(context);
        context.setVersionMajor(8);
        context.setVersionMinor(20);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_G1_PRIOR_U40.getKey()),
                Analysis.WARN_JDK8_G1_PRIOR_U40 + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_G1_PRIOR_U40_RECS.getKey()),
                Analysis.WARN_JDK8_G1_PRIOR_U40_RECS + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis incorrectly identified.");
    }

    @Test
    void testJdk8GcLogFileRotationDisabledOverwrite() {
        String opts = "-XX:+PrintGC -Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps "
                + "-XX:+PrintGCApplicationStoppedTime -XX:-UseGCLogFileRotation";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_GC_LOG_FILE_OVERWRITE.getKey()),
                Analysis.WARN_JDK8_GC_LOG_FILE_OVERWRITE + " analysis not identified.");
    }

    @Test
    void testJdk8GcLogFileRotationNotEnabledOverwrite() {
        String opts = "-XX:+PrintGC -Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps "
                + "-XX:+PrintGCApplicationStoppedTime";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_GC_LOG_FILE_OVERWRITE.getKey()),
                Analysis.WARN_JDK8_GC_LOG_FILE_OVERWRITE + " analysis not identified.");
    }

    @Test
    void testJdk8LogFileSizeSmall() {
        String opts = "-Xss128k -Xloggc:gc.log -XX:NumberOfGCLogFiles=5 -XX:+UseGCLogFileRotation "
                + "-XX:GCLogFileSize=8192";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_GC_LOG_FILE_SIZE_SMALL.getKey()),
                Analysis.WARN_JDK8_GC_LOG_FILE_SIZE_SMALL + " analysis not identified.");
    }

    @Test
    void testJdk8NotG1PriorUpdate40() {
        String opts = "";
        JvmContext context = new JvmContext(opts);
        List<GarbageCollector> collectors = new ArrayList<GarbageCollector>();
        collectors.add(GarbageCollector.CMS);
        context.setGarbageCollectors(collectors);
        context.setVersionMajor(8);
        context.setVersionMinor(20);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_G1_PRIOR_U40.getKey()),
                Analysis.WARN_JDK8_G1_PRIOR_U40 + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_G1_PRIOR_U40_RECS.getKey()),
                Analysis.WARN_JDK8_G1_PRIOR_U40_RECS + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis incorrectly identified.");
    }

    /**
     * Test JDK8 with PARALLEL_OLD collector disabled.
     */
    @Test
    void testJdk8ParallelSerialOldDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD.getKey()),
                Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_CRUFT.getKey()),
                Analysis.INFO_PARALLEL_OLD_CRUFT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_GC_SERIAL_ELECTED.getKey()),
                Analysis.INFO_GC_SERIAL_ELECTED + " analysis incorrectly identified.");
    }

    @Test
    void testJdk8PrintGCDetailsMissingLogGc() {
        String opts = "-Xss128k -Xloggc:gc.log -Xms2048M";
        JvmContext context = new JvmContext(opts, 8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_PRINT_GC_DETAILS_MISSING.getKey()),
                Analysis.WARN_JDK8_PRINT_GC_DETAILS_MISSING + " analysis not identified.");
    }

    @Test
    void testJdk8PrintGCDetailsMissingLogGcMissing() {
        String opts = "-Xss128k -Xms2048M";
        JvmContext context = new JvmContext(opts, 8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_PRINT_GC_DETAILS_MISSING.getKey()),
                Analysis.WARN_JDK8_PRINT_GC_DETAILS_MISSING + " analysis incorrectly identified.");
    }

    @Test
    void testJdk8Update40() {
        String opts = "-XX:+UnlockExperimentalVMOptions";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(8);
        context.setVersionMinor(40);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_G1_PRIOR_U40.getKey()),
                Analysis.WARN_JDK8_G1_PRIOR_U40 + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_G1_PRIOR_U40_RECS.getKey()),
                Analysis.WARN_JDK8_G1_PRIOR_U40_RECS + " analysis incorrectly identified.");
    }

    @Test
    void testJfrFlightRecorderOptions() {
        String opts = "-Xss128k -XX:FlightRecorderOptions=stackdepth=256 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JFR.getKey()), Analysis.INFO_JFR + " analysis not identified.");
    }

    @Test
    void testJfrStartFlightRecording() {
        String opts = "-Xss128k -XX:StartFlightRecording=duration=200s,filename=flight.jfr -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JFR.getKey()), Analysis.INFO_JFR + " analysis not identified.");
    }

    @Test
    void testJmxManagementServerEnabled() {
        String opts = "-Xss128k -XX:+ManagementServer -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JMX_ENABLED.getKey()),
                Analysis.INFO_JMX_ENABLED + " analysis not identified.");
    }

    @Test
    void testJmxSystemProperty() {
        String opts = "-Xss128k -Dcom.sun.management.jmxremote -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JMX_ENABLED.getKey()),
                Analysis.INFO_JMX_ENABLED + " analysis not identified.");
    }

    @Test
    void testJvmOptionsCollectorIgnored() {
        String opts = "-Xss128k -XX:+UseG1GC";
        JvmContext context = new JvmContext(opts);
        context.getGarbageCollectors().add(GarbageCollector.PARALLEL_SCAVENGE);
        context.getGarbageCollectors().add(GarbageCollector.PARALLEL_OLD);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_GC_SERIAL_ELECTED.getKey()),
                Analysis.INFO_GC_SERIAL_ELECTED + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_GC_IGNORED.getKey()),
                Analysis.INFO_GC_IGNORED + " analysis not identified.");
    }

    @Test
    void testLargePagesG1Windows() {
        String opts = "-XX:+UseLargePages";
        JvmContext context = new JvmContext(opts);
        // JDK11 default collector is G1
        context.setVersionMajor(11);
        context.setOs(Os.WINDOWS);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_LARGE_PAGES_G1_WINDOWS.getKey()),
                Analysis.WARN_LARGE_PAGES_G1_WINDOWS + " analysis not identified.");
    }

    @Test
    void testLargePagesHeapLarge() {
        String opts = "-Xss128k -Xmx4097M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_LARGE_PAGES_CONSIDER.getKey()),
                Analysis.INFO_LARGE_PAGES_CONSIDER + " analysis not identified.");
    }

    @Test
    void testLargePagesHeapLargeUseHugeTlbfs() {
        String opts = "-Xss128k -XX:+UseHugeTLBFS -Xmx4097M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_LARGE_PAGES_CONSIDER.getKey()),
                Analysis.INFO_LARGE_PAGES_CONSIDER + " analysis incorrectly identified.");
    }

    @Test
    void testLargePagesHeapLargeUseLargePages() {
        String opts = "-Xss128k -XX:+UseLargePages -Xmx4097M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_LARGE_PAGES_CONSIDER.getKey()),
                Analysis.INFO_LARGE_PAGES_CONSIDER + " analysis incorrectly identified.");
    }

    @Test
    void testLargePagesHeapLargeUseTransparentHugePages() {
        String opts = "-Xss128k -XX:+UseTransparentHugePages -Xmx4097M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_LARGE_PAGES_CONSIDER.getKey()),
                Analysis.INFO_LARGE_PAGES_CONSIDER + " analysis incorrectly identified.");
    }

    @Test
    void testLargePagesHeapSmall() {
        String opts = "-Xss128k -Xmx4096M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_LARGE_PAGES_CONSIDER.getKey()),
                Analysis.INFO_LARGE_PAGES_CONSIDER + " analysis incorrectly identified.");
    }

    @Test
    void testLargePagesHugeTlbfsThps() {
        String opts = "-XX:+UseHugeTLBFS -XX:+UseTransparentHugePages";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_LARGE_PAGES_LINUX_HUGETLB_THP.getKey()),
                Analysis.ERROR_LARGE_PAGES_LINUX_HUGETLB_THP + " analysis not identified.");
    }

    @Test
    void testLargePagesShmThps() {
        String opts = "-XX:+UseSHM -XX:+UseTransparentHugePages";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_LARGE_PAGES_LINUX_HUGETLB_THP.getKey()),
                Analysis.ERROR_LARGE_PAGES_LINUX_HUGETLB_THP + " analysis not identified.");
    }

    @Test
    void testLargePagesThps() {
        String opts = "-XX:+UseLargePages -XX:+UseTransparentHugePages";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_LARGE_PAGES_LINUX_HUGETLB_THP.getKey()),
                Analysis.ERROR_LARGE_PAGES_LINUX_HUGETLB_THP + " analysis not identified.");
    }

    @Test
    void testLogFileNumberWithRotationDisabled() {
        String opts = "-Xss128k -XX:NumberOfGCLogFiles=5 -XX:-UseGCLogFileRotation -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_GC_LOG_FILE_ROTATION_DISABLED.getKey()),
                Analysis.WARN_JDK8_GC_LOG_FILE_ROTATION_DISABLED + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_GC_LOG_FILE_ROTATION_DISABLED_NUM.getKey()),
                Analysis.WARN_JDK8_GC_LOG_FILE_ROTATION_DISABLED_NUM + " analysis not identified.");
    }

    @Test
    void testLogFileSizeSmall() {
        String opts = "-Xss128k -Xlog:gc*:file=/path/to/gc.log:time,level,tags:filecount=0,filesize=4M -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK11_GC_LOG_FILE_SIZE_SMALL.getKey()),
                Analysis.WARN_JDK11_GC_LOG_FILE_SIZE_SMALL + " analysis not identified.");
    }

    @Test
    void testLogFileSizeSmallNot() {
        String opts = "-Xss128k -Xlog:gc*:file=/path/to/gc.log:time,level,tags:filecount=0 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK11_GC_LOG_FILE_SIZE_SMALL.getKey()),
                Analysis.WARN_JDK11_GC_LOG_FILE_SIZE_SMALL + " analysis incorrectly identified.");
    }

    @Test
    void testMaxFdLimitIgnored() {
        String opts = "-Xss128k -Xmx2048M -XX:+MaxFDLimit";
        JvmContext context = new JvmContext(opts);
        context.setOs(Os.LINUX);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_MAX_FD_LIMIT_IGNORED.getKey()),
                Analysis.INFO_MAX_FD_LIMIT_IGNORED + " analysis not identified.");
    }

    @Test
    void testMaxFdLimitOsUnknown() {
        String opts = "-Xss128k -Xmx2048M -XX:+MaxFDLimit";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_MAX_FD_LIMIT_IGNORED.getKey()),
                Analysis.INFO_MAX_FD_LIMIT_IGNORED + " analysis incorrectly identified.");
    }

    @Test
    void testMaxFdLimitSolaris() {
        String opts = "-Xss128k -Xmx2048M -XX:+MaxFDLimit";
        JvmContext context = new JvmContext(opts);
        context.setOs(Os.SOLARIS);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_MAX_FD_LIMIT_IGNORED.getKey()),
                Analysis.INFO_MAX_FD_LIMIT_IGNORED + " analysis incorrectly identified.");
    }

    @Test
    void testMaxMetaspaceSizeLessThanCompressedClassSpaceSize() {
        String opts = "-XX:MaxMetaspaceSize=256m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_METASPACE_LT_COMP_CLASS.getKey()),
                Analysis.WARN_METASPACE_LT_COMP_CLASS + " analysis not identified.");
        String warnMetaspaceLtCompClass = "MaxMetaspaceSize < CompressedClassSpaceSize, resulting in the JVM adjusting "
                + "down the Class Metadata and Compressed Class Space sizes as follows: CompressedClassSpaceSize' = "
                + "MaxMetaspaceSize(256M) - [2 * InitialBootClassLoaderMetaspaceSize(4M)] = 248M. Class Metadata Size' "
                + "= MaxMetaspaceSize(256M) - CompressedClassSpaceSize'(248M) = 8M.";
        assertEquals(warnMetaspaceLtCompClass,
                jvmOptions.getAnalysisLiteral(Analysis.WARN_METASPACE_LT_COMP_CLASS.getKey()),
                Analysis.WARN_METASPACE_LT_COMP_CLASS + " not correct.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_METASPACE_LT_COMP_CLASS.getKey()),
                Analysis.WARN_METASPACE_LT_COMP_CLASS + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE.getKey()),
                Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE + " analysis not identified.");
        String infoMetaspacClassMetadataAndCompClassSpace = "Metaspace(256M) = Class Metadata(8M) + Compressed Class "
                + "Space(248M).";
        assertEquals(infoMetaspacClassMetadataAndCompClassSpace,
                jvmOptions.getAnalysisLiteral(Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE.getKey()),
                Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE + " not correct.");
    }

    @Test
    void testMaxPermSize() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_MAX_PERM_SIZE.getKey()),
                Analysis.INFO_MAX_PERM_SIZE + " analysis not identified.");
    }

    @Test
    void testMetaspaceEq32g() {
        String opts = "-Xss512 -Xmx32g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_METASPACE_CLASS_METADATA.getKey()),
                Analysis.INFO_METASPACE_CLASS_METADATA + " analysis not identified.");
    }

    @Test
    void testMetaspaceGt32G() {
        String opts = "-Xss512 -Xmx33g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_METASPACE_CLASS_METADATA.getKey()),
                Analysis.INFO_METASPACE_CLASS_METADATA + " analysis not identified.");
    }

    @Test
    void testMetaspaceLt32g() {
        String opts = "-Xss512 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE.getKey()),
                Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE + " analysis not identified.");
    }

    @Test
    void testMetaspaceUndefinedCompressedOopsDisabled() {
        String opts = "-Xmx1g -XX:-UseCompressedOops";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_METASPACE_CLASS_METADATA.getKey()),
                Analysis.INFO_METASPACE_CLASS_METADATA + " analysis not identified.");
        String infoMetaspacClassMetadataAndCompClassSpace = "Metaspace = Class Metadata only (no Compressed Class "
                + "Space).";
        assertEquals(infoMetaspacClassMetadataAndCompClassSpace,
                jvmOptions.getAnalysisLiteral(Analysis.INFO_METASPACE_CLASS_METADATA.getKey()),
                Analysis.INFO_METASPACE_CLASS_METADATA + " not correct.");
    }

    @Test
    void testMetaspaceUnlimitedCompressedClassSize512M() {
        String opts = "-Xmx1g -XX:CompressedClassSpaceSize=512m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE.getKey()),
                Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE + " analysis not identified.");
        String infoMetaspacClassMetadataAndCompClassSpace = "Metaspace(unlimited) = Class Metadata(unlimited) + "
                + "Compressed Class Space(512M).";
        assertEquals(infoMetaspacClassMetadataAndCompClassSpace,
                jvmOptions.getAnalysisLiteral(Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE.getKey()),
                Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE + " not correct.");
    }

    @Test
    void testMetaspaceUnlimitedCompressedClassSizeDefault() {
        String opts = "-Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE.getKey()),
                Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_METASPACE_LT_COMP_CLASS.getKey()),
                Analysis.WARN_METASPACE_LT_COMP_CLASS + " analysis incorrectly identified.");
        String infoMetaspacClassMetadataAndCompClassSpace = "Metaspace(unlimited) = Class Metadata(unlimited) + "
                + "Compressed Class Space(1024M).";
        assertEquals(infoMetaspacClassMetadataAndCompClassSpace,
                jvmOptions.getAnalysisLiteral(Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE.getKey()),
                Analysis.INFO_METASPACE_CLASS_METADATA_AND_COMP_CLASS_SPACE + " not correct.");
    }

    @Test
    void testMinHeapDeltaBytes() {
        String opts = "-Xms1g -XX:MinHeapDeltaBytes=12345 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_MIN_HEAP_DELTA_BYTES.getKey()),
                Analysis.INFO_MIN_HEAP_DELTA_BYTES + " analysis not identified.");
    }

    /**
     * Test analysis if native library being used.
     */
    @Test
    void testNative() {
        String opts = "-Xss128k -Xms2048M -agentpath:/path/to/agent.so -Xmx2048";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_NATIVE_AGENT.getKey()),
                Analysis.INFO_NATIVE_AGENT + " analysis not identified.");
    }

    /**
     * Test analysis if new space &gt; old space.
     */
    @Test
    void testNewRatioInverted() {
        String opts = "-Xss128k -Xmx4g -XX:NewSize=2g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_NEW_RATIO_INVERTED.getKey()),
                Analysis.INFO_NEW_RATIO_INVERTED + " analysis not identified.");
    }

    @Test
    void testOmitStackTraceInFastThrowDisabled() {
        String opts = "-Xss128k -XX:-OmitStackTraceInFastThrow -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_OMIT_STACK_TRACE_IN_FAST_THROW_DISABLED.getKey()),
                Analysis.WARN_OMIT_STACK_TRACE_IN_FAST_THROW_DISABLED + " analysis not identified.");
    }

    @Test
    void testOnOutOfMemoryError() {
        String opts = "-Xss128k -XX:OnOutOfMemoryError=\"/usr/bin/restart_tomcat\" -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_ON_OOME_KILL.getKey()),
                Analysis.INFO_ON_OOME_KILL + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_ON_OOME.getKey()),
                Analysis.INFO_ON_OOME + " analysis not identified.");
    }

    @Test
    void testOptsUndefined() {
        String opts = "-XX:-Mike";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_OPTS_UNDEFINED.getKey()),
                Analysis.INFO_OPTS_UNDEFINED + " analysis not identified.");
        assertEquals("Undefined JVM option(s): -XX:-Mike.",
                jvmOptions.getAnalysisLiteral(Analysis.INFO_OPTS_UNDEFINED.getKey()),
                Analysis.INFO_UNACCOUNTED_OPTIONS_DISABLED + " not correct.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_UNACCOUNTED_OPTIONS_DISABLED.getKey()),
                Analysis.INFO_UNACCOUNTED_OPTIONS_DISABLED + " analysis incorrectly identified.");
    }

    @Test
    void testParalleGcThreads() {
        String opts = "-Xss128k -XX:ParallelGCThreads=4 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_GC_THREADS_1.getKey()),
                Analysis.ERROR_PARALLEL_GC_THREADS_1 + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_GC_THREADS_SERIAL.getKey()),
                Analysis.INFO_PARALLEL_GC_THREADS_SERIAL + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_GC_THREADS.getKey()),
                Analysis.INFO_PARALLEL_GC_THREADS + " analysis not identified.");
    }

    @Test
    void testParalleGcThreads1() {
        String opts = "-Xss128k -XX:ParallelGCThreads=1 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_GC_THREADS_1.getKey()),
                Analysis.ERROR_PARALLEL_GC_THREADS_1 + " analysis not identified.");
    }

    @Test
    void testParalleGcThreadsSerial() {
        String opts = "-Xss128k -XX:+UseSerialGC -XX:ParallelGCThreads=1 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_GC_THREADS_1.getKey()),
                Analysis.ERROR_PARALLEL_GC_THREADS_1 + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_GC_THREADS_SERIAL.getKey()),
                Analysis.INFO_PARALLEL_GC_THREADS_SERIAL + " analysis not identified.");
    }

    @Test
    void testParallelClassLoading() {
        String opts = "-Xss128k -XX:+UnlockDiagnosticVMOptions -XX:+UnsyncloadClass -Xmx2G";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_DIAGNOSTIC_UNSYNCLOAD_CLASS.getKey()),
                Analysis.WARN_DIAGNOSTIC_UNSYNCLOAD_CLASS + " analysis not identified.");
    }

    @Test
    void testParallelInitialMarkDisabledCms() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:-CMSParallelInitialMarkEnabled";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CMS_PARALLEL_INITIAL_MARK_DISABLED.getKey()),
                Analysis.WARN_CMS_PARALLEL_INITIAL_MARK_DISABLED + " analysis not identified.");
    }

    @Test
    void testParallelInitialMarkDisabledCmsDisabled() {
        String opts = "-Xms1024m -Xmx2048m -XX:-UseConcMarkSweepGC -XX:-CMSParallelInitialMarkEnabled";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CMS_PARALLEL_INITIAL_MARK_DISABLED.getKey()),
                Analysis.WARN_CMS_PARALLEL_INITIAL_MARK_DISABLED + " analysis incorrectly identified.");
    }

    /**
     * Test if PARALLEL_OLD collector disabled with -XX:-UseParallelOldGC.
     */
    @Test
    void testParallelOldDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseParallelGC -XX:-UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD.getKey()),
                Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD + " analysis not identified.");
    }

    /**
     * Test if PARALLEL_OLD collector disabled when using default collector on JDK8.
     */
    @Test
    void testParallelOldDisabledJdk8() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD.getKey()),
                Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD + " analysis not identified.");
    }

    /**
     * Test if PARALLEL_OLD collector is enabled/disabled when the parallel collector is not used. Parallel is the
     * default collector in JDK8.
     */
    @Test
    void testParallelOldfCruftJdk8() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_CRUFT.getKey()),
                Analysis.INFO_PARALLEL_OLD_CRUFT + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_REDUNDANT.getKey()),
                Analysis.INFO_PARALLEL_OLD_REDUNDANT + " analysis not identified.");
    }

    /**
     * Test if PARALLEL_OLD collector is enabled/disabled when the parallel collector is not used false positive.
     */
    @Test
    void testParallelOldfCruftNone() {
        String opts = "-Xss128k -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_CRUFT.getKey()),
                Analysis.INFO_PARALLEL_OLD_CRUFT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD.getKey()),
                Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD + " analysis incorrectly identified.");
    }

    /**
     * Specify -XX:+UseParallelOldGC will cause the default collector to not be used on JDK11.
     */
    @Test
    void testParallelOldfCruftNoneJdk11() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(11);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_CRUFT.getKey()),
                Analysis.INFO_PARALLEL_OLD_CRUFT + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_CRUFT.getKey()),
                Analysis.INFO_PARALLEL_OLD_CRUFT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_REDUNDANT.getKey()),
                Analysis.INFO_PARALLEL_OLD_REDUNDANT + " analysis incorrectly identified.");

    }

    /**
     * Test -XX:+UseParallelOldGC overriding -XX:-UseParallelGC.
     */
    @Test
    void testParallelOldGcOverrideCruftNone() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseParallelGC -XX:+UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_CRUFT.getKey()),
                Analysis.INFO_PARALLEL_OLD_CRUFT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD.getKey()),
                Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD + " analysis incorrectly identified.");
    }

    @Test
    void testParallelRemarkEnabledCms() {
        String opts = "-Xms1024m -Xmx2048m -XX:MaxPermSize=256m -XX:-CMSParallelRemarkEnabled";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_CMS_PARALLEL_REMARK_DISABLED.getKey()),
                Analysis.WARN_CMS_PARALLEL_REMARK_DISABLED + " analysis not identified.");
    }

    @Test
    void testParallelRemarkEnabledCmsDisabled() {
        String opts = "-Xms1024m -Xmx2048m -XX:-UseConcMarkSweepGC -XX:-CMSParallelRemarkEnabled";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_CMS_PARALLEL_REMARK_DISABLED.getKey()),
                Analysis.WARN_CMS_PARALLEL_REMARK_DISABLED + " analysis incorrectly identified.");
    }

    /**
     * Test PARALLEL_SCAVENGE with PARALLEL_OLD collector disabled.
     */
    @Test
    void testParallelScavengeParallelSerialOld() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseParallelGC -XX:-UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        context.getGarbageCollectors().add(GarbageCollector.PARALLEL_SCAVENGE);
        context.getGarbageCollectors().add(GarbageCollector.PARALLEL_SERIAL_OLD);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD.getKey()),
                Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_CRUFT.getKey()),
                Analysis.INFO_PARALLEL_OLD_CRUFT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_GC_SERIAL_ELECTED.getKey()),
                Analysis.INFO_GC_SERIAL_ELECTED + " analysis incorrectly identified.");
    }

    @Test
    void testParNewCmsSerialOldIgnoredConcurrentModeFailure() {
        String opts = "-Xss128k -Xmx2048M -XX:-UseParallelOldGC -XX:+UseConcMarkSweepGC "
                + "-XX:+ExplicitGCInvokesConcurrent";
        JvmContext context = new JvmContext(opts);
        context.getGarbageCollectors().add(GarbageCollector.PAR_NEW);
        context.getGarbageCollectors().add(GarbageCollector.CMS);
        context.getGarbageCollectors().add(GarbageCollector.SERIAL_OLD);
        context.setVersionMajor(8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_CMS_MISSING.getKey()),
                Analysis.ERROR_CMS_MISSING + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD.getKey()),
                Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_PAR_NEW_SERIAL_OLD.getKey()),
                Analysis.ERROR_PAR_NEW_SERIAL_OLD + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT.getKey()),
                Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_GC_SERIAL_ELECTED.getKey()),
                Analysis.INFO_GC_SERIAL_ELECTED + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_CRUFT.getKey()),
                Analysis.INFO_PARALLEL_OLD_CRUFT + " analysis not identified.");
        assertNull(jvmOptions.getDuplicates(), "Duplicate options incorrectly identified.");
    }

    @Test
    void testParNewSerialOld() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseParNewGC -XX:-UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        context.getGarbageCollectors().add(GarbageCollector.PAR_NEW);
        context.getGarbageCollectors().add(GarbageCollector.SERIAL_OLD);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_CMS_MISSING.getKey()),
                Analysis.ERROR_CMS_MISSING + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD.getKey()),
                Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_PAR_NEW_SERIAL_OLD.getKey()),
                Analysis.ERROR_PAR_NEW_SERIAL_OLD + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT.getKey()),
                Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_GC_SERIAL_ELECTED.getKey()),
                Analysis.INFO_GC_SERIAL_ELECTED + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_CRUFT.getKey()),
                Analysis.INFO_PARALLEL_OLD_CRUFT + " analysis incorrectly identified.");
    }

    @Test
    void testPerfDataDisabled() {
        String opts = "-Xss512 -XX:-UsePerfData -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PERF_DATA_DISABLED.getKey()),
                Analysis.INFO_PERF_DATA_DISABLED + " analysis not identified.");
    }

    @Test
    void testPermSize() {
        String opts = "-Xms1024m -Xmx2048m -XX:PermSize=256m";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PERM_SIZE.getKey()),
                Analysis.INFO_PERM_SIZE + " analysis not identified.");
    }

    @Test
    void testPrintAdaptiveSizePolicyDisabled() {
        String opts = "-Xss128k -Xmx4g -XX:-PrintAdaptiveSizePolicy";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_ADAPTIVE_RESIZE_PLCY_DISABLED.getKey()),
                Analysis.INFO_JDK8_PRINT_ADAPTIVE_RESIZE_PLCY_DISABLED + " analysis not identified.");
    }

    @Test
    void testPrintAdaptiveSizePolicyEnabled() {
        String opts = "-Xss128k -Xmx4g -XX:+PrintAdaptiveSizePolicy";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_ADAPTIVE_RESIZE_PLCY_ENABLED.getKey()),
                Analysis.INFO_JDK8_PRINT_ADAPTIVE_RESIZE_PLCY_ENABLED + " analysis not identified.");
    }

    @Test
    void testPrintClassHistogram() {
        String opts = "-Xss128k -Xmx2048M -XX:+PrintClassHistogram";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_PRINT_CLASS_HISTOGRAM.getKey()),
                Analysis.WARN_PRINT_CLASS_HISTOGRAM + " analysis not identified.");
    }

    @Test
    void testPrintClassHistogramAfterFullGc() {
        String opts = "-Xss128k -Xmx2048M -XX:+PrintClassHistogramAfterFullGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_PRINT_CLASS_HISTOGRAM_AFTER_FULL_GC.getKey()),
                Analysis.WARN_PRINT_CLASS_HISTOGRAM_AFTER_FULL_GC + " analysis not identified.");
    }

    @Test
    void testPrintClassHistogramBeforeFullGc() {
        String opts = "-Xss128k -Xmx2048M -XX:+PrintClassHistogramBeforeFullGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_PRINT_CLASS_HISTOGRAM_BEFORE_FULL_GC.getKey()),
                Analysis.WARN_PRINT_CLASS_HISTOGRAM_BEFORE_FULL_GC + " analysis not identified.");
    }

    @Test
    void testPrintFLSStatistics() {
        String opts = "-Xss128k -XX:PrintFLSStatistics=1 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_FLS_STATISTICS.getKey()),
                Analysis.INFO_JDK8_PRINT_FLS_STATISTICS + " analysis not identified.");
    }

    /**
     * Test if explicit not GC handled concurrently.
     */
    @Test
    void testPrintGCApplicationConcurrentTime() {
        String opts = "-Xss128k -Xms2048M -Xmx4096M -XX:+PrintGCApplicationConcurrentTime";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PRINT_GC_APPLICATION_CONCURRENT_TIME.getKey()),
                Analysis.INFO_PRINT_GC_APPLICATION_CONCURRENT_TIME + " analysis not identified.");
    }

    @Test
    void testPrintGcCause() {
        String opts = "-Xms1024m -Xmx2048m -XX:+PrintGCCause -XX:-HeapDumpOnOutOfMemoryError";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_GC_CAUSE.getKey()),
                Analysis.INFO_JDK8_PRINT_GC_CAUSE + " analysis not identified.");
    }

    @Test
    void testPrintGcCauseDisabled() {
        String opts = "-Xms1024m -Xmx2048m -XX:-PrintGCCause -XX:-HeapDumpOnOutOfMemoryError";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_PRINT_GC_CAUSE_DISABLED.getKey()),
                Analysis.WARN_JDK8_PRINT_GC_CAUSE_DISABLED + " analysis not identified.");
    }

    /**
     * Test with PrintGCDetails disabled with -XX:-PrintGCDetails.
     */
    @Test
    void testPrintGCDetailsDisabled() {
        String opts = "-Xss128k -Xloggc:gc.log -XX:-PrintGCDetails -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_PRINT_GC_DETAILS_DISABLED.getKey()),
                Analysis.WARN_JDK8_PRINT_GC_DETAILS_DISABLED + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_PRINT_GC_DETAILS_MISSING.getKey()),
                Analysis.WARN_JDK8_PRINT_GC_DETAILS_MISSING + " analysis incorrectly identified.");
    }

    @Test
    void testPrintGCDetailsMissing() {
        String opts = "-Xss128k -Xms2048M -Xloggc:gc.log";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_PRINT_GC_DETAILS_MISSING.getKey()),
                Analysis.WARN_JDK8_PRINT_GC_DETAILS_MISSING + " analysis not identified.");
    }

    @Test
    void testPrintGCDetailsNotMissing() {
        String opts = "-Xss128k -XX:+PrintGCDetails -Xms2048M -Xloggc:gc.log";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_PRINT_GC_DETAILS_MISSING.getKey()),
                Analysis.WARN_JDK8_PRINT_GC_DETAILS_MISSING + " analysis identified.");
    }

    @Test
    void testPrintHeapAtGc() {
        String opts = "-Xss128k -XX:+PrintHeapAtGC -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_HEAP_AT_GC.getKey()),
                Analysis.INFO_JDK8_PRINT_HEAP_AT_GC + " analysis not identified.");
    }

    @Test
    void testPrintNMTStatistics() {
        String opts = "-Xss128k -XX:+UnlockDiagnosticVMOptions -XX:+PrintNMTStatistics -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED.getKey()),
                Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED + " analysis not identified.");
        String diagnostic = "Diagnostic options. The following should be removed when relevant troubleshooting is "
                + "completed, as they add additional overhead and are not recommended/supported for general production "
                + "use: -XX:+UnlockDiagnosticVMOptions -XX:+PrintNMTStatistics.";
        assertEquals(diagnostic, jvmOptions.getAnalysisLiteral(Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED.getKey()),
                Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED + " not correct.");
    }

    @Test
    void testPrintPromotionFailure() {
        String opts = "-Xss128k -Xmx4g -XX:+PrintPromotionFailure";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_PROMOTION_FAILURE.getKey()),
                Analysis.INFO_JDK8_PRINT_PROMOTION_FAILURE + " analysis not identified.");
    }

    /**
     * Test if -XX:+PrintReferenceGC enabled
     */
    @Test
    void testPrintReferenceGC() {
        String opts = "-Xss128k -Xmx2048M -XX:+PrintReferenceGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_REFERENCE_GC_ENABLED.getKey()),
                Analysis.INFO_JDK8_PRINT_REFERENCE_GC_ENABLED + " analysis not identified.");
    }

    /**
     * Test if -XX:+PrintStringDeduplicationStatistics enabled
     */
    @Test
    void testPrintStringDeduplicationStatistics() {
        String opts = "-Xss128k -XX:+PrintStringDeduplicationStatistics -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_STRING_DEDUP_STATS_ENABLED.getKey()),
                Analysis.INFO_JDK8_PRINT_STRING_DEDUP_STATS_ENABLED + " analysis not identified.");
    }

    /**
     * Test if -XX:+PrintStringTableStatistics enabled
     */
    @Test
    void testPrintStringTableStatistics() {
        String opts = "-Xss128k -XX:+PrintStringTableStatistics -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_STRING_TABLE_STATS_ENABLED.getKey()),
                Analysis.INFO_JDK8_PRINT_STRING_TABLE_STATS_ENABLED + " analysis not identified.");
    }

    @Test
    void testPrintTenuringDistribution() {
        String opts = "-Xss128k -XX:+PrintTenuringDistribution -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_TENURING_DISTRIBUTION.getKey()),
                Analysis.INFO_JDK8_PRINT_TENURING_DISTRIBUTION + " analysis not identified.");
    }

    @Test
    void testPrintTenuringDistributionDisabled() {
        String opts = "-Xss128k -XX:-PrintTenuringDistribution -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_TENURING_DISTRIBUTION.getKey()),
                Analysis.INFO_JDK8_PRINT_TENURING_DISTRIBUTION + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_PRINT_TENURING_DISTRIBUTION_DISABLED.getKey()),
                Analysis.INFO_JDK8_PRINT_TENURING_DISTRIBUTION_DISABLED + " analysis not identified.");
        assertNull(jvmOptions.getUnaccountedDisabledOptions(), "Unaccounted disabled options incorrect.");
    }

    /**
     * Verify analysis file property key/value lookup.
     */
    @Test
    void testPropertyKeyValueLookup() {
        Analysis[] analysis = Analysis.values();
        for (int i = 0; i < analysis.length; i++) {
            assertNotNull(analysis[i].getKey() + " not found.", analysis[i].getValue());
        }
    }

    @Test
    void testRamPctInital100() {
        String opts = "-Xss128k -XX:InitialRAMPercentage=100.000000 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        context.setVersionMajor(11);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_RAM_PCT_INITIAL_100.getKey()),
                Analysis.ERROR_RAM_PCT_INITIAL_100 + " analysis not identified.");
    }

    @Test
    void testRamPctInitalGtMax() {
        String opts = "-Xss128k -XX:InitialRAMPercentage=90.000000 -XX:MaxRAMPercentage=60.000000 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        context.setVersionMajor(11);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_RAM_PCT_INITIAL_GT_MAX.getKey()),
                Analysis.ERROR_RAM_PCT_INITIAL_GT_MAX + " analysis not identified.");
    }

    @Test
    void testRamPctInitalGtMin() {
        String opts = "-Xss128k -XX:InitialRAMPercentage=100.000000 -XX:MinRAMPercentage=60.000000 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        context.setVersionMajor(11);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_RAM_PCT_INITIAL_GT_MIN.getKey()),
                Analysis.ERROR_RAM_PCT_INITIAL_GT_MIN + " analysis not identified.");
    }

    @Test
    void testRamPctMax100() {
        String opts = "-Xss128k -XX:MaxRAMPercentage=100.000000 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        context.setVersionMajor(11);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_RAM_PCT_MAX_100.getKey()),
                Analysis.ERROR_RAM_PCT_MAX_100 + " analysis not identified.");
    }

    @Test
    void testRamPctMin100() {
        String opts = "-Xss128k -XX:MinRAMPercentage=100.000000 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        context.setVersionMajor(11);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_RAM_PCT_MIN_100.getKey()),
                Analysis.ERROR_RAM_PCT_MIN_100 + " analysis not identified.");
    }

    @Test
    void testSafepointLogging() {
        String opts = "-Xss128k -XX:+UnlockDiagnosticVMOptions -XX:+PrintSafepointStatistics "
                + "-XX:PrintSafepointStatisticsCount=1 -XX:+LogVMOutput -XX:LogFile=/path/to/vm.log -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED.getKey()),
                Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_DIAGNOSTIC_PRINT_SAFEPOINT_STATISTICS.getKey()),
                Analysis.WARN_DIAGNOSTIC_PRINT_SAFEPOINT_STATISTICS + " analysis not identified.");
        String diagnostic = "Diagnostic options. The following should be removed when relevant troubleshooting is "
                + "completed, as they add additional overhead and are not recommended/supported for general production "
                + "use: -XX:+UnlockDiagnosticVMOptions -XX:+PrintSafepointStatistics -XX:+LogVMOutput.";
        assertEquals(diagnostic, jvmOptions.getAnalysisLiteral(Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED.getKey()),
                Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED + " not correct.");
    }

    @Test
    void testScavengeBeforeFullGcIgnored() {
        String opts = "-Xss512 -XX:+ScavengeBeforeFullGC -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(11);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_SCAVENGE_BEFORE_FULL_GC_IGNORED.getKey()),
                Analysis.INFO_SCAVENGE_BEFORE_FULL_GC_IGNORED + " analysis not identified.");
    }

    @Test
    void testScavengeBeforeFullGcRedundant() {
        String opts = "-Xss512 -XX:+ScavengeBeforeFullGC -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        context.setVersionMajor(8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_SCAVENGE_BEFORE_FULL_GC_REDUNDANT.getKey()),
                Analysis.INFO_SCAVENGE_BEFORE_FULL_GC_REDUNDANT + " analysis not identified.");
    }

    @Test
    void testSerialNewCms() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseConcMarkSweepGC -XX:-UseParNewGC";
        JvmContext context = new JvmContext(opts);
        context.getGarbageCollectors().add(GarbageCollector.SERIAL_NEW);
        context.getGarbageCollectors().add(GarbageCollector.CMS);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_JDK8_CMS_PAR_NEW_DISABLED.getKey()),
                Analysis.ERROR_JDK8_CMS_PAR_NEW_DISABLED + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_GC_SERIAL_ELECTED.getKey()),
                Analysis.INFO_GC_SERIAL_ELECTED + " analysis incorrectly identified.");
    }

    @Test
    void testServerFlag() {
        String opts = "-Xss512 -server -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        // 64-bit is assumed
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_64_SERVER_REDUNDANT.getKey()),
                Analysis.INFO_64_SERVER_REDUNDANT + " analysis not identified.");
    }

    @Test
    void testShenandoahGuaranteedGCInterval() {
        String opts = "-XX:+UnlockExperimentalVMOptions -XX:ShenandoahGuaranteedGCInterval=20000";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
        String warnExperimental = "Experimental options. Consider removing the following, as they are not "
                + "recommended/supported in production: -XX:+UnlockExperimentalVMOptions "
                + "-XX:ShenandoahGuaranteedGCInterval=20000.";
        assertEquals(warnExperimental,
                jvmOptions.getAnalysisLiteral(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " not correct.");
    }

    @Test
    void testShenandoahUncommitDelay() {
        String opts = "-XX:+UnlockExperimentalVMOptions -XX:ShenandoahUncommitDelay=5000";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
        String warnExperimental = "Experimental options. Consider removing the following, as they are not "
                + "recommended/supported in production: -XX:+UnlockExperimentalVMOptions "
                + "-XX:ShenandoahUncommitDelay=5000.";
        assertEquals(warnExperimental,
                jvmOptions.getAnalysisLiteral(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " not correct.");
    }

    @Test
    void testSignalHandlingDisabled() {
        String opts = "-Xss512 -Xmx33g -Xrs";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_RS.getKey()), Analysis.WARN_RS + " analysis not identified.");
    }

    @Test
    void testSurvivorRatio() {
        String opts = "-Xss128k -XX:SurvivorRatio=6 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_SURVIVOR_RATIO.getKey()),
                Analysis.INFO_SURVIVOR_RATIO + " analysis not identified.");
    }

    @Test
    void testSystemPropertyJdkTlsDisabledAlgorithms() {
        String opts = "-Xss128k -Djdk.tls.disabledAlgorithms=TLSv1 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.getSystemProperties().contains("-Djdk.tls.disabledAlgorithms=TLSv1"),
                "System property jdk.tls.disabledAlgorithms not found.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_SYSTEM_PROPERTY_JDK_TLS_DISABLED_ALGORITHMS.getKey()),
                Analysis.ERROR_SYSTEM_PROPERTY_JDK_TLS_DISABLED_ALGORITHMS + " analysis not identified.");
    }

    @Test
    void testTargetSurvivorRatio() {
        String opts = "-Xss128k -XX:TargetSurvivorRatio=90 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_SURVIVOR_RATIO_TARGET.getKey()),
                Analysis.INFO_SURVIVOR_RATIO_TARGET + " analysis not identified.");
    }

    @Test
    void testTenuringDisabled() {
        String opts = "-Xss128k -XX:MaxTenuringThreshold=0 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_TENURING_DISABLED.getKey()),
                Analysis.WARN_TENURING_DISABLED + " analysis not identified.");
    }

    @Test
    void testTenuringMissing() {
        String opts = "-Xss128k -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_MAX_TENURING_OVERRIDE.getKey()),
                Analysis.INFO_MAX_TENURING_OVERRIDE + " analysis incorrectly identified.");
    }

    @Test
    void testTenuringNotDefault() {
        String opts = "-Xss128k -XX:MaxTenuringThreshold=8 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_MAX_TENURING_OVERRIDE.getKey()),
                Analysis.INFO_MAX_TENURING_OVERRIDE + " analysis not identified.");
    }

    @Test
    void testThreadPriorityPolicyAggressive() {
        String opts = "-Xss128k -XX:ThreadPriorityPolicy=1 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_PRIORITY_POLICY_AGGRESSIVE.getKey()),
                Analysis.WARN_THREAD_PRIORITY_POLICY_AGGRESSIVE + " analysis not identified.");
    }

    @Test
    void testThreadPriorityPolicyAggressiveBackdoor() {
        String opts = "-Xss128k -XX:ThreadPriorityPolicy=42 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_PRIORITY_POLICY_AGGRESSIVE_BACKDOOR.getKey()),
                Analysis.WARN_THREAD_PRIORITY_POLICY_AGGRESSIVE_BACKDOOR + " analysis not identified.");
    }

    @Test
    void testThreadPriorityPolicyBad() {
        String opts = "-Xss128k -XX:+UseThreadPriorities -XX:ThreadPriorityPolicy=-1 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_USE_THREAD_PRIORITIES_REDUNDANT.getKey()),
                Analysis.INFO_USE_THREAD_PRIORITIES_REDUNDANT + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_PRIORITY_POLICY_BAD.getKey()),
                Analysis.WARN_THREAD_PRIORITY_POLICY_BAD + " analysis not identified.");
    }

    @Test
    void testThreadPriorityPolicyIgnored() {
        String opts = "-Xss128k -XX:-UseThreadPriorities -XX:ThreadPriorityPolicy=1 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_PRIORITY_POLICY_IGNORED.getKey()),
                Analysis.WARN_THREAD_PRIORITY_POLICY_IGNORED + " analysis not identified.");
    }

    @Test
    void testThreadPriorityPolicyMissing() {
        String opts = "-Xss128k -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_USE_THREAD_PRIORITIES_REDUNDANT.getKey()),
                Analysis.INFO_USE_THREAD_PRIORITIES_REDUNDANT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_PRIORITY_POLICY_BAD.getKey()),
                Analysis.WARN_THREAD_PRIORITY_POLICY_BAD + " analysis incorrectly identified.");
    }

    @Test
    void testThreadPriorityPolicyRedundant() {
        String opts = "-Xss128k -XX:ThreadPriorityPolicy=0 -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_THREAD_PRIORITY_POLICY_REDUNDANT.getKey()),
                Analysis.INFO_THREAD_PRIORITY_POLICY_REDUNDANT + " analysis not identified.");
    }

    @Test
    void testThreadStackSizeNotSetBit32() {
        String opts = "-Xmx2048M";
        JvmContext context = new JvmContext(opts);
        context.setBit(Bit.BIT32);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_STACK_SIZE_NOT_SET_32.getKey()),
                Analysis.WARN_THREAD_STACK_SIZE_NOT_SET_32 + " analysis not identified.");
    }

    @Test
    void testThreadStackSizeNotSetBit64() {
        String opts = "-Xmx2048M";
        JvmContext context = new JvmContext(opts);
        context.setBit(Bit.BIT64);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_STACK_SIZE_NOT_SET_32.getKey()),
                Analysis.WARN_THREAD_STACK_SIZE_NOT_SET_32 + " analysis incorrectly identified.");
    }

    @Test
    void testThreadStackSizeNotSetBitUnknown() {
        // 64-bit is assumed
        String opts = "-Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_STACK_SIZE_NOT_SET_32.getKey()),
                Analysis.WARN_THREAD_STACK_SIZE_NOT_SET_32 + " analysis incorrectly identified.");
    }

    @Test
    void testThreadStackSizeTiny() {
        String opts = "-Xss512 -XX:TargetSurvivorRatio=90 -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_THREAD_STACK_SIZE_TINY.getKey()),
                Analysis.WARN_THREAD_STACK_SIZE_TINY + " analysis not identified.");
    }

    @Test
    void testTieredCompilation() {
        String opts = "-Xss128k -XX:+TieredCompilation -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_TIERED_COMPILATION_ENABLED.getKey()),
                Analysis.INFO_TIERED_COMPILATION_ENABLED + " analysis not identified.");
    }

    @Test
    void testTieredCompilationDisabled() {
        String opts = "-Xss128k -XX:-TieredCompilation -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_UNACCOUNTED_OPTIONS_DISABLED.getKey()),
                Analysis.INFO_UNACCOUNTED_OPTIONS_DISABLED + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_TIERED_COMPILATION_DISABLED.getKey()),
                Analysis.INFO_TIERED_COMPILATION_DISABLED + " analysis not identified.");
    }

    @Test
    void testTraceClassLoading() {
        String opts = "-Xss128k -XX:+TraceClassLoading -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_TRACE_CLASS_LOADING.getKey()),
                Analysis.INFO_TRACE_CLASS_LOADING + " analysis not identified.");
    }

    @Test
    void testTraceClassLoadingDisabled() {
        String opts = "-Xss128k -XX:-TraceClassLoading -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_TRACE_CLASS_LOADING_DISABLED.getKey()),
                Analysis.INFO_TRACE_CLASS_LOADING_DISABLED + " analysis not identified.");
        assertNull(jvmOptions.getUnaccountedDisabledOptions(), "Unaccounted disabled options incorrect.");
    }

    @Test
    void testTraceClassUnloading() {
        String opts = "-Xss128k -XX:+TraceClassUnloading -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_TRACE_CLASS_UNLOADING.getKey()),
                Analysis.INFO_TRACE_CLASS_UNLOADING + " analysis not identified.");
    }

    @Test
    void testTraceClassUnloadingDisabled() {
        String opts = "-Xss128k -XX:-TraceClassUnloading -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_TRACE_CLASS_UNLOADING_DISABLED.getKey()),
                Analysis.INFO_TRACE_CLASS_UNLOADING_DISABLED + " analysis not identified.");
        assertNull(jvmOptions.getUnaccountedDisabledOptions(), "Unaccounted disabled options incorrect.");
    }

    @Test
    void testTransparentHugePages() {
        String opts = "-XX:+UseTransparentHugePages";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_THP.getKey()), Analysis.WARN_THP + " analysis not identified.");
    }

    @Test
    void testUnaccountedOptionsDisabled() {
        String opts = "-Xss128K -XX:-BackgroundCompilation -Xms1024m -Xmx2048m -XX:-UseCompressedClassPointers "
                + "-XX:-UseCompressedOops -XX:-Mike";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        // Remove fake JVM option to similate that it is defined
        jvmOptions.getUndefined().remove(0);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_UNACCOUNTED_OPTIONS_DISABLED.getKey()),
                Analysis.INFO_UNACCOUNTED_OPTIONS_DISABLED + " analysis not identified.");
        assertEquals("Unaccounted disabled JVM options: -XX:-Mike.",
                jvmOptions.getAnalysisLiteral(Analysis.INFO_UNACCOUNTED_OPTIONS_DISABLED.getKey()),
                "Unaccount options disabled not correct.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_OPTS_UNDEFINED.getKey()),
                Analysis.INFO_OPTS_UNDEFINED + " analysis incorrectly identified.");
    }

    @Test
    void testUnlockDiagnosticVMOptions() {
        String opts = "-Xss128k -XX:+UnlockDiagnosticVMOptions -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED.getKey()),
                Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testUnlockExperimentalVMOptions() {
        String opts = "-Xss128k -XX:+UnlockExperimentalVMOptions -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testUseAdaptiveSizePolicyDisabled() {
        String opts = "-Xms1024m -Xmx2048m -XX:-UseAdaptiveSizePolicy";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_ADAPTIVE_SIZE_POLICY_DISABLED.getKey()),
                Analysis.WARN_ADAPTIVE_SIZE_POLICY_DISABLED + " analysis not identified.");
    }

    @Test
    void testUseCmsCompactAtFullCollectionEnabled() {
        String opts = "-Xss128k -XX:+UseCMSCompactAtFullCollection -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_JDK8_USE_CMS_COMPACTION_AT_FULL_GC_ENABLED.getKey()),
                Analysis.ERROR_JDK8_USE_CMS_COMPACTION_AT_FULL_GC_ENABLED + " analysis not identified.");
    }

    @Test
    void testUseCodeCacheFlushingDisabled() {
        String opts = "-Xss128k -XX:-UseCodeCacheFlushing -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_USE_CODE_CACHE_FLUSHING_DISABLED.getKey()),
                Analysis.INFO_USE_CODE_CACHE_FLUSHING_DISABLED + " analysis not identified.");
        assertNull(jvmOptions.getUnaccountedDisabledOptions(),
                "-XX:-UseCodeCacheFlushing incorrectly identified as an unaccounted disabled option.");
    }

    @Test
    void testUseCondCardMark() {
        String opts = "-Xss128k -XX:+UseCondCardMark -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_USE_COND_CARD_MARK.getKey()),
                Analysis.WARN_USE_COND_CARD_MARK + " analysis not identified.");
    }

    @Test
    void testUseFastUnorderedTimeStampsErgonomics() {
        String opts = "-XX:+UseFastUnorderedTimeStamps";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_FAST_UNORDERED_TIMESTAMPS.getKey()),
                Analysis.WARN_FAST_UNORDERED_TIMESTAMPS + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_FAST_UNORDERED_TIMESTAMPS.getKey()),
                Analysis.INFO_FAST_UNORDERED_TIMESTAMPS + " analysis not identified.");
    }

    @Test
    void testUseFastUnorderedTimeStampsUnlockExperimental() {
        String opts = "-XX:+UnlockExperimentalVMOptions -XX:+UseFastUnorderedTimeStamps";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_FAST_UNORDERED_TIMESTAMPS.getKey()),
                Analysis.WARN_FAST_UNORDERED_TIMESTAMPS + " analysis not identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED.getKey()),
                Analysis.WARN_EXPERIMENTAL_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

    @Test
    void testUseG1DoesNotIdentifySerialOld() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseG1GC";
        JvmContext context = new JvmContext(opts);
        context.getGarbageCollectors().add(GarbageCollector.G1);
        context.setVersionMajor(8);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD.getKey()),
                Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD + " analysis incorrectly identified.");
    }

    @Test
    void testUseHugeTLBFS() {
        String opts = "-XX:+UseHugeTLBFS";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_LARGE_PAGES_LINUX_HUGETLBFS.getKey()),
                Analysis.INFO_LARGE_PAGES_LINUX_HUGETLBFS + " analysis not identified.");
    }

    @Test
    void testUseLargePages() {
        String opts = "-XX:+UseLargePages";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_LARGE_PAGES.getKey()),
                Analysis.INFO_LARGE_PAGES + " analysis not identified.");
    }

    @Test
    void testUseLargePagesIndividualAllocationDisabled() {
        String opts = "-Xmx1g -XX:-UseLargePagesIndividualAllocation";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_UNACCOUNTED_OPTIONS_DISABLED.getKey()),
                Analysis.INFO_UNACCOUNTED_OPTIONS_DISABLED + " analysis incorrectly identified.");
    }

    @Test
    void testUseLargePagesInMetaspace() {
        String opts = "-XX:+UseLargePagesInMetaspace";
        JvmContext context = new JvmContext(opts);
        context.setContainer(true);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_USE_LARGE_PAGES_IN_METASPACE.getKey()),
                Analysis.INFO_USE_LARGE_PAGES_IN_METASPACE + " analysis not identified.");
    }

    @Test
    void testUseLargePagesLinux() {
        String opts = "-XX:+UseLargePages";
        JvmContext context = new JvmContext(opts);
        context.setOs(Os.LINUX);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_LARGE_PAGES.getKey()),
                Analysis.INFO_LARGE_PAGES + " analysis incorrectly identified.");
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_LARGE_PAGES_LINUX_HUGETLBFS.getKey()),
                Analysis.INFO_LARGE_PAGES_LINUX_HUGETLBFS + " analysis not identified.");
    }

    @Test
    void testUseMembar() {
        String opts = "-Xss128k -Xms2048M -Xmx4096M -XX:+UseMembar";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_USE_MEMBAR.getKey()),
                Analysis.WARN_USE_MEMBAR + " analysis not identified.");
    }

    @Test
    void testUseParallelOldGcDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseParallelGC -XX:-UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD.getKey()),
                Analysis.ERROR_PARALLEL_SCAVENGE_PARALLEL_SERIAL_OLD + " analysis not identified.");
    }

    @Test
    void testUseParallelOldGcRedundant() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseParallelGC -XX:+UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_PARALLEL_OLD_REDUNDANT.getKey()),
                Analysis.INFO_PARALLEL_OLD_REDUNDANT + " analysis not identified.");
    }

    /**
     * Test if PAR_NEW collector disabled with -XX:-UseParNewGC with the CMS collector.
     */
    @Test
    void testUseParNewGcDisabled() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseConcMarkSweepGC -XX:-UseParNewGC "
                + "-XX:CMSInitiatingOccupancyFraction=70";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.ERROR_JDK8_CMS_PAR_NEW_DISABLED.getKey()),
                Analysis.ERROR_JDK8_CMS_PAR_NEW_DISABLED + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT.getKey()),
                Analysis.INFO_JDK8_CMS_PAR_NEW_CRUFT + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_CMS_DISABLED.getKey()),
                Analysis.INFO_CMS_DISABLED + " analysis incorrectly identified.");
    }

    @Test
    void testUseParNewGcRedundant() {
        String opts = "-Xss128k -Xmx2048M -XX:+UseConcMarkSweepGC -XX:+UseParNewGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_JDK8_CMS_PAR_NEW_REDUNDANT.getKey()),
                Analysis.INFO_JDK8_CMS_PAR_NEW_REDUNDANT + " analysis not identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_CMS_DISABLED.getKey()),
                Analysis.INFO_CMS_DISABLED + " analysis incorrectly identified.");
        assertFalse(jvmOptions.hasAnalysis(Analysis.ERROR_JDK8_CMS_PAR_NEW_DISABLED.getKey()),
                Analysis.ERROR_JDK8_CMS_PAR_NEW_DISABLED + " analysis incorrectly identified.");
    }

    @Test
    void testUseSHM() {
        String opts = "-XX:+UseSHM";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_LARGE_PAGES_LINUX_SHM.getKey()),
                Analysis.WARN_LARGE_PAGES_LINUX_SHM + " analysis not identified.");
    }

    @Test
    void testUseStringDeduplicationDisabledCMS() {
        String opts = "-Xss128k -XX:+UseStringDeduplication -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        context.getGarbageCollectors().add(GarbageCollector.CMS);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_USE_STRING_DEDUPLICATION_UNSUPPORTED.getKey()),
                Analysis.INFO_USE_STRING_DEDUPLICATION_UNSUPPORTED + " analysis not identified.");
    }

    @Test
    void testUseStringDeduplicationDisabledDefaultCollectorJdk11() {
        String opts = "-Xss128k -XX:+UseStringDeduplication -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        context.setVersionMajor(11);
        jvmOptions.doAnalysis();
        assertFalse(jvmOptions.hasAnalysis(Analysis.INFO_USE_STRING_DEDUPLICATION_REDUNDANT.getKey()),
                Analysis.INFO_USE_STRING_DEDUPLICATION_REDUNDANT + " analysis incorrectly identified.");
    }

    @Test
    void testUseStringDeduplicationDisabledDefaultCollectorJdk8() {
        String opts = "-Xss128k -XX:+UseStringDeduplication -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        context.setVersionMajor(8);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_USE_STRING_DEDUPLICATION_UNSUPPORTED.getKey()),
                Analysis.INFO_USE_STRING_DEDUPLICATION_UNSUPPORTED + " analysis not identified.");
    }

    @Test
    void testUseStringDeduplicationDisabledG1() {
        String opts = "-Xss128k -XX:-UseStringDeduplication -Xmx2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        context.getGarbageCollectors().add(GarbageCollector.G1);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_USE_STRING_DEDUPLICATION_REDUNDANT.getKey()),
                Analysis.INFO_USE_STRING_DEDUPLICATION_REDUNDANT + " analysis not identified.");
        assertNull(jvmOptions.getUnaccountedDisabledOptions(),
                "-XX:-UseStringDeduplication incorrectly identified as an unaccounted disabled option.");
    }

    @Test
    void testUseThreadPrioritiesDisabled() {
        String opts = "-Xss128k -XX:-UseThreadPriorities -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_USE_THREAD_PRIORITIES_DISABLED.getKey()),
                Analysis.WARN_USE_THREAD_PRIORITIES_DISABLED + " analysis not identified.");
    }

    @Test
    void testUseThreadPrioritiesRedundant() {
        String opts = "-Xss128k -XX:+UseThreadPriorities -Xms2048M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_USE_THREAD_PRIORITIES_REDUNDANT.getKey()),
                Analysis.INFO_USE_THREAD_PRIORITIES_REDUNDANT + " analysis not identified.");
    }

    @Test
    void testUseTransparentHugePages() {
        String opts = "-XX:+UseTransparentHugePages";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_LARGE_PAGES_LINUX_THPS.getKey()),
                Analysis.INFO_LARGE_PAGES_LINUX_THPS + " analysis not identified.");
    }

    @Test
    void testUseVmInterruptibleIo() {
        String opts = "-Xss512 -Xmx33g -XX:-UseVMInterruptibleIO";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.WARN_JDK8_USE_VM_INTERRUPTIBLE_IO.getKey()),
                Analysis.WARN_JDK8_USE_VM_INTERRUPTIBLE_IO + " analysis not identified.");
    }

    @Test
    void testVerboseClass() {
        String opts = "-Xss128k -verbose:class -Xmx2G";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_VERBOSE_CLASS.getKey()),
                Analysis.INFO_VERBOSE_CLASS + " analysis not identified.");
    }

    @Test
    void testZStatisticsInterval() {
        String opts = "-XX:+UseZGC -XX:+UnlockDiagnosticVMOptions -XX:ZStatisticsInterval=100 -Xmx10G";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        jvmOptions.doAnalysis();
        assertTrue(jvmOptions.hasAnalysis(Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED.getKey()),
                Analysis.INFO_DIAGNOSTIC_VM_OPTIONS_ENABLED + " analysis not identified.");
    }

}
