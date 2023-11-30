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
package org.github.joa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.github.joa.domain.GarbageCollector;
import org.github.joa.domain.JvmContext;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class TestJvmOptions {
    @Test
    void test7NewOptions() {
        String opts = "-XX:MaxJavaStackTraceDepth=50000 -XX:MaxGCPauseMillis=500 -XX:G1HeapRegionSize=4m "
                + "-XX:+UseStringDeduplication -XX:OnOutOfMemoryError=\"pmap %p\"  -XX:+DebugNonSafepoints "
                + "-XX:FlightRecorderOptions=stackdepth=256";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MaxJavaStackTraceDepth=50000", jvmOptions.getMaxJavaStackTraceDepth(),
                "MaxJavaStackTraceDepth not correct.");
        assertEquals("-XX:MaxGCPauseMillis=500", jvmOptions.getMaxGcPauseMillis(), "MaxGCPauseMillis not correct.");
        assertEquals("-XX:G1HeapRegionSize=4m", jvmOptions.getG1HeapRegionSize(), "G1HeapRegionSize not correct.");
        assertEquals("-XX:+UseStringDeduplication", jvmOptions.getUseStringDeduplication(),
                "UseStringDeduplication not correct.");
        assertEquals("-XX:OnOutOfMemoryError=\"pmap %p\"", jvmOptions.getOnOutOfMemoryError(),
                "OnOutOfMemoryError not correct.");
        assertEquals("-XX:+DebugNonSafepoints", jvmOptions.getDebugNonSafepoints(), "DebugNonSafepoints not correct.");
        assertEquals("-XX:FlightRecorderOptions=stackdepth=256", jvmOptions.getFlightRecorderOptions(),
                "FlightRecorderOptions not correct.");
    }

    @Test
    void testActiveProcessorCount() {
        String opts = "-Xms1g -XX:ActiveProcessorCount=2 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:ActiveProcessorCount=2", jvmOptions.getActiveProcessorCount(),
                "ActiveProcessorCount not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testAddExports() {
        String opts = "-Xmx1g --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("--add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED", jvmOptions.getAddExports().get(0),
                "--add-exports not correct.");
    }

    @Test
    void testAgentLib() {
        String opts = "-Xms1g -agentlib:am_sun_16=/path/to/my.properties -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-agentlib:am_sun_16=/path/to/my.properties", jvmOptions.getAgentlib().get(0),
                "JDPA socket transport (debugging) not correct.");
    }

    @Test
    void testCheckJni() {
        String opts = "-Xms1g -Xcheck:jni -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertTrue(jvmOptions.isCheckJni(), "check:jni not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testClasspath() {
        String opts = "-Xmx1g -classpath /path/to/tomcat/bin/bootstrap.jar:/path/to/tomcat/bin/tomcat-juli.jar:"
                + "/path/to/java/ant.jar:/path/to/java/ant-launcher.jar:/path/to/java/lib/tools.jar -Xss512k";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals(0, jvmOptions.getUndefined().size(), "Unknown options found.");
        assertEquals(
                "-classpath /path/to/tomcat/bin/bootstrap.jar:/path/to/tomcat/bin/tomcat-juli.jar:"
                        + "/path/to/java/ant.jar:/path/to/java/ant-launcher.jar:/path/to/java/lib/tools.jar",
                jvmOptions.getClasspath(), "classpath not correct.");
    }

    @Test
    void testCmsIncrementalMode() {
        String opts = "-Xms1000m -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -Xmx1500m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+CMSIncrementalMode", jvmOptions.getCmsIncrementalMode(), "CMSIncrementalMode not correct.");
    }

    @Test
    void testCmsIncrementalPacing() {
        String opts = "-Xms1000m -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:+CMSIncrementalPacing -Xmx1500m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+CMSIncrementalPacing", jvmOptions.getCmsIncrementalPacing(),
                "CMSIncrementalPacing not correct.");
    }

    @Test
    void testCMSScavengeBeforeRemark() {
        String opts = "-Xms1g -XX:+CMSScavengeBeforeRemark -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+CMSScavengeBeforeRemark", jvmOptions.getCmsScavengeBeforeRemark(),
                "CMSScavengeBeforeRemark not correct.");
    }

    @Test
    void testCncurrentio() {
        String opts = "-Xms1g -Xconcurrentio -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertTrue(jvmOptions.isConcurrentio(), "concurrentio not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testCommonOptions() {
        String opts = "-Xmx1500m -Xms1000m -Xss512k -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=2048m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xss512k", jvmOptions.getThreadStackSize(), "Thread stack size not correct.");
        assertEquals("-Xms1000m", jvmOptions.getInitialHeapSize(), "Initial heap size not correct.");
        assertEquals("-Xmx1500m", jvmOptions.getMaxHeapSize(), "Max heap size not correct.");
        assertEquals("-XX:MetaspaceSize=256M", jvmOptions.getMetaspaceSize(), "Metaspace size not correct.");
        assertEquals("-XX:MaxMetaspaceSize=2048m", jvmOptions.getMaxMetaspaceSize(), "Max metaspace size not correct.");
    }

    @Test
    void testCompileCommandFile() {
        String opts = "-Xms1g -XX:CompileCommandFile=/etc/cassandra/conf/hotspot_compiler -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:CompileCommandFile=/etc/cassandra/conf/hotspot_compiler", jvmOptions.getCompileCommandFile(),
                "CompileCommandFile not correct.");
    }

    @Test
    void testCompileThreshold() {
        String opts = "-Xms1g -XX:CompileThreshold=5000 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:CompileThreshold=5000", jvmOptions.getCompileThreshold(), "CompileThreshold not correct.");
    }

    @Test
    void testCompressedClassPointers() {
        String opts = "-Xmx1g -XX:+UseCompressedOops -XX:+UseCompressedClassPointers";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertTrue(jvmOptions.isCompressedClassPointers(), "Compressed class pointers not identified.");
    }

    @Test
    void testCompressedClassPointers32g() {
        String opts = "-Xmx1g -XX:+UseCompressedOops -XX:+UseCompressedClassPointers -Xmx32g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertFalse(jvmOptions.isCompressedClassPointers(), "Compressed class pointers incorrectly identified.");
    }

    @Test
    void testCompressedClassPointersCompressedOopsDisabled() {
        String opts = "-Xmx1g -XX:-UseCompressedOops -XX:+UseCompressedClassPointers";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertFalse(jvmOptions.isCompressedClassPointers(), "Compressed class pointers incorrectly identified.");
    }

    @Test
    void testCompressedClassPointersDisabled() {
        String opts = "-Xmx1g -XX:+UseCompressedOops -XX:-UseCompressedClassPointers";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertFalse(jvmOptions.isCompressedClassPointers(), "Compressed class pointers incorrectly identified.");
    }

    @Test
    void testCompressedOops32g() {
        String opts = "-Xmx1g -XX:+UseCompressedOops -XX:+UseCompressedClassPointers -Xmx32g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertFalse(jvmOptions.isCompressedOops(), "Compressed oops incorrectly identified.");
    }

    @Test
    void testCompressedOopsDisabled() {
        String opts = "-Xmx1g -XX:-UseCompressedOops -XX:+UseCompressedClassPointers";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertFalse(jvmOptions.isCompressedOops(), "Compressed oops incorrectly identified.");
    }

    @Test
    void testCompressedOopsUseCompressedClassPointersDisabled() {
        String opts = "-Xmx1g -XX:+UseCompressedOops -XX:-UseCompressedClassPointers";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertTrue(jvmOptions.isCompressedOops(), "Compressed oops not identified.");
    }

    @Test
    void testCrashOnOutOfMemoryError() {
        String opts = "-Xms1g -XX:+CrashOnOutOfMemoryError -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+CrashOnOutOfMemoryError", jvmOptions.getCrashOnOutOfMemoryError(),
                "CrashOnOutOfMemoryError not correct.");
    }

    @Test
    void testDisableAttachMechanism() {
        String opts = "-XX:+DisableAttachMechanism";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+DisableAttachMechanism", jvmOptions.getDisableAttachMechanism(),
                "DisableAttachMechanism not correct.");
    }

    @Test
    void testDisabledOptions() {
        String opts = "-Xss128K -XX:-BackgroundCompilation -Xms1024m -Xmx2048m -XX:-UseCompressedClassPointers "
                + "-XX:-UseCompressedOops -XX:-TraceClassUnloading";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);

        assertEquals(4, jvmOptions.getDisabledOptions().size(), "Disabled options count incorrect.");
        assertTrue(jvmOptions.getDisabledOptions().contains("-XX:-BackgroundCompilation"),
                "-XX:-BackgroundCompilation not identified as disabled option.");
        assertTrue(jvmOptions.getDisabledOptions().contains("-XX:-UseCompressedClassPointers"),
                "-XX:-UseCompressedClassPointers not identified as disabled option.");
        assertTrue(jvmOptions.getDisabledOptions().contains("-XX:-UseCompressedOops"),
                "-XX:-UseCompressedOops not identified as disabled option.");
        assertTrue(jvmOptions.getDisabledOptions().contains("-XX:-TraceClassUnloading"),
                "-XX:-TraceClassUnloading not identified as disabled option.");
        assertNull(jvmOptions.getUnaccountedDisabledOptions(), "Unaccounted disabled options incorrect.");
    }

    @Test
    void testDisableExplicitGCNotOverridingExplicitGCInvokesConcurrent() {
        String opts = "-Xms1g -XX:+DisableExplicitGC -XX:+ExplicitGCInvokesConcurrent -Xmx2g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertFalse(
                jvmOptions.isOverriding(jvmOptions.getExplicitGCInvokesConcurrent(), jvmOptions.getDisableExplicitGc()),
                "-XX:+DisableExplicitGC overriding -XX:+ExplicitGCInvokesConcurrent incorrectly detected.");
    }

    @Test
    void testDisableExplicitGCOverridingExplicitGCInvokesConcurrent() {
        String opts = "-Xms1g -XX:+ExplicitGCInvokesConcurrent -XX:+DisableExplicitGC -Xmx2g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertTrue(
                jvmOptions.isOverriding(jvmOptions.getExplicitGCInvokesConcurrent(), jvmOptions.getDisableExplicitGc()),
                "-XX:+DisableExplicitGC overriding -XX:+ExplicitGCInvokesConcurrent not detected.");
    }

    @Test
    void testDisableJvmSignalHandling() {
        String opts = "-Xms1g -Xrs -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertTrue(jvmOptions.isRs(), "rs not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testDuplicateAddExports() {
        String opts = "-Xms1g --add-exports=java.base/sun.nio.ch=ALL-UNNAMED "
                + "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED -Xmx2g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("--add-exports=java.base/sun.nio.ch=ALL-UNNAMED --add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
                jvmOptions.getDuplicates(), "Duplicates not correct.");
    }

    @Test
    void testDuplicateBootclasspath() {
        String opts = "-Xms1g -Xbootclasspath/p:/path/to/jar1 -Xbootclasspath/p:/path/to/jar2 -Xmx2g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertNull(jvmOptions.getDuplicates(), "Duplicates not correct.");
    }

    @Test
    void testDuplicateXms() {
        String opts = "-Xms1g -Xms2g -Xmaxjitcodesize1G -Xmx2g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xms1g -Xms2g", jvmOptions.getDuplicates(), "Duplicates not correct.");
    }

    @Test
    void testDuplicateXmxMaxHeap() {
        String opts = "-XX:MaxHeapSize=2048m -Xmx4096m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MaxHeapSize=2048m -Xmx4096m", jvmOptions.getDuplicates(), "Duplicates not correct.");
    }

    @Test
    void testG1ConcRefinementThreads() {
        String opts = "-Xmx1500m -Xms1000m -XX:G1ConcRefinementThreads=4";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:G1ConcRefinementThreads=4", jvmOptions.getG1ConcRefinementThreads(),
                "G1ConcRefinementThreads not correct.");
    }

    @Test
    void testG1MixedGCCountTarget() {
        String opts = "-Xms1000m -XX:G1MixedGCCountTarget=4  -Xmx1500m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:G1MixedGCCountTarget=4", jvmOptions.getG1MixedGCCountTarget(),
                "G1MixedGCCountTarget  not correct.");
    }

    @Test
    void testG1NewSizePercent() {
        String opts = "-Xmx1g -XX:+UnlockExperimentalVMOptions -XX:G1NewSizePercent=1";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:G1NewSizePercent=1", jvmOptions.getG1NewSizePercent(), "G1NewSizePercent not correct.");
    }

    @Test
    void testG1PeriodicGCInterval() {
        String opts = "-Xms1g -XX:G1PeriodicGCInterval=8000 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:G1PeriodicGCInterval=8000", jvmOptions.getG1PeriodicGCInterval(),
                "G1PeriodicGCInterval not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testG1PeriodicGCIntervalWithUnits() {
        String opts = "-Xms1g -XX:G1PeriodicGCInterval=8k -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:G1PeriodicGCInterval=8k", jvmOptions.getG1PeriodicGCInterval(),
                "G1PeriodicGCInterval not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testG1ReservePercent() {
        String opts = "-Xms1g -XX:G1ReservePercent=10 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:G1ReservePercent=10", jvmOptions.getG1ReservePercent(), "G1ReservePercent not correct.");
    }

    @Test
    void testGarbageCollectorG1() {
        String opts = "-Xmx1500m -Xms1000m -XX:+UseG1GC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals(1, jvmOptions.getExpectedGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.G1),
                GarbageCollector.G1 + " collector not identified.");
    }

    @Test
    void testGarbageCollectorsConcMarkSweep() {
        String opts = "-Xmx1500m -Xms1000m -XX:+UseConcMarkSweepGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals(3, jvmOptions.getExpectedGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.PAR_NEW),
                GarbageCollector.PAR_NEW + " collector not identified.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.CMS),
                GarbageCollector.CMS + " collector not identified.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.SERIAL_OLD),
                GarbageCollector.SERIAL_OLD + " collector not identified.");
    }

    @Test
    void testGarbageCollectorShenandoah() {
        String opts = "-Xmx1500m -Xms1000m -XX:+UseShenandoahGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals(1, jvmOptions.getExpectedGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.SHENANDOAH),
                GarbageCollector.G1 + " collector not identified.");
    }

    @Test
    void testGarbageCollectorsParallelScavengeParallelOld() {
        String opts = "-Xmx1500m -Xms1000m -XX:+UseParallelGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals(2, jvmOptions.getExpectedGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.PARALLEL_SCAVENGE),
                GarbageCollector.PARALLEL_SCAVENGE + " collector not identified.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.PARALLEL_OLD),
                GarbageCollector.PARALLEL_OLD + " collector not identified.");
        opts = "-Xmx1500m -Xms1000m -XX:+UseParallelOldGC";
        jvmOptions = new JvmOptions(context);
        assertEquals(2, jvmOptions.getExpectedGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.PARALLEL_SCAVENGE),
                GarbageCollector.PARALLEL_SCAVENGE + " collector not identified.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.PARALLEL_OLD),
                GarbageCollector.PARALLEL_OLD + " collector not identified.");
        opts = "-Xmx1500m -Xms1000m -XX:+UseParallelGC -XX:+UseParallelOldGC";
        jvmOptions = new JvmOptions(context);
        assertEquals(2, jvmOptions.getExpectedGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.PARALLEL_SCAVENGE),
                GarbageCollector.PARALLEL_SCAVENGE + " collector not identified.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.PARALLEL_OLD),
                GarbageCollector.PARALLEL_OLD + " collector not identified.");
    }

    @Test
    void testGarbageCollectorsParallelScavengeParallelSerialOld() {
        String opts = "-Xmx1500m -Xms1000m -XX:+UseParallelGC -XX:-UseParallelOldGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals(2, jvmOptions.getExpectedGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.PARALLEL_SCAVENGE),
                GarbageCollector.PARALLEL_SCAVENGE + " collector not identified.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.PARALLEL_SERIAL_OLD),
                GarbageCollector.PARALLEL_SERIAL_OLD + " collector not identified.");
    }

    @Test
    void testGarbageCollectorsParNewConcurrentMarkSweep() {
        String opts = "-Xmx1500m -Xms1000m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals(3, jvmOptions.getExpectedGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.PAR_NEW),
                GarbageCollector.PAR_NEW + " collector not identified.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.CMS),
                GarbageCollector.CMS + " collector not identified.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.SERIAL_OLD),
                GarbageCollector.SERIAL_OLD + " collector not identified.");
    }

    @Test
    void testGarbageCollectorsParNewSerialOld() {
        String opts = "-Xmx1500m -Xms1000m -XX:+UseParNewGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals(2, jvmOptions.getExpectedGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.PAR_NEW),
                GarbageCollector.PAR_NEW + " collector not identified.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.SERIAL_OLD),
                GarbageCollector.SERIAL_OLD + " collector not identified.");
    }

    @Test
    void testGarbageCollectorsSerialSerialOld() {
        String opts = "-Xmx1500m -Xms1000m -XX:+UseSerialGC";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals(2, jvmOptions.getExpectedGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.SERIAL_NEW),
                GarbageCollector.SERIAL_NEW + " collector not identified.");
        assertTrue(jvmOptions.getExpectedGarbageCollectors().contains(GarbageCollector.SERIAL_OLD),
                GarbageCollector.SERIAL_OLD + " collector not identified.");
    }

    @Test
    void testGCLockerRetryAllocationCount() {
        String opts = "-Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:GCLockerRetryAllocationCount=21";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:GCLockerRetryAllocationCount=21", jvmOptions.getGcLockerRetryAllocationCount(),
                "GCLockerRetryAllocationCount not correct.");
    }

    @Test
    void testGcLoggingOptions() {
        String opts = "-Xmx1500m -Xms1000m -verbose:gc -Xloggc:/path/to/EAP-7.1.0/standalone/log/gc.log "
                + "-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 "
                + "-XX:GCLogFileSize=3M -Xss512k";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xloggc:/path/to/EAP-7.1.0/standalone/log/gc.log", jvmOptions.getLoggc(),
                "Gc log location not correct.");
        assertEquals(true, jvmOptions.isVerboseGc(), "-verbose:gc not correct.");
        assertEquals("-XX:NumberOfGCLogFiles=5", jvmOptions.getNumberOfGcLogFiles(), "NumberOfGCLogFiles not correct.");
        assertEquals("-XX:GCLogFileSize=3M", jvmOptions.getGcLogFileSize(), "GCLogFileSize not correct.");
        assertEquals("-XX:+UseGCLogFileRotation", jvmOptions.getUseGcLogFileRotation(),
                "UseGCLogFileRotation not correct.");
        assertEquals("-XX:+PrintGCDetails", jvmOptions.getPrintGcDetails(), "PrintGCDetails not correct.");
        assertEquals("-XX:+PrintGCDateStamps", jvmOptions.getPrintGcDateStamps(), "PrintGCDateStamps not correct.");
    }

    @Test
    void testGcLoggingToStdOutJdk11() {
        String opts = "-Xms1g -Xlog:all=info:stdout:uptime,levels,tags -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertTrue(jvmOptions.isGcLoggingToStdout(), "GC logging to stdout not identified.");
    }

    @Test
    void testGcLoggingToStdOutJdk8() {
        String opts = "-Xms1g -XX:+PrintGC -XX:+PrintGCDetails -XX:-PrintGCTimeStamps -XX:+PrintGCDateStamps "
                + "-XX:+PrintGCApplicationStoppedTime -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertTrue(jvmOptions.isGcLoggingToStdout(), "GC logging to stdout not identified.");
    }

    @Test
    void testGcLoggingToStdOutJdk8Not() {
        String opts = "-Xms1g -XX:+PrintGC -XX:+PrintGCDetails -XX:-PrintGCTimeStamps -XX:+PrintGCDateStamps "
                + "-XX:+PrintGCApplicationStoppedTime -Xloggc:gc_%p_%t.log -XX:+UseGCLogFileRotation "
                + "-XX:GCLogFileSize=50M -XX:NumberOfGCLogFiles=4 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertFalse(jvmOptions.isGcLoggingToStdout(), "GC logging to stdout incorrectly identified.");
    }

    @Test
    void testGcLoggingToStdOutNotJdk11() {
        String opts = "-Xms1g -Xlog:gc*,safepoint:file=/path/to/gc.log:time,level,tags:filecount=0 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertFalse(jvmOptions.isGcLoggingToStdout(), "GC logging to stdout incorrectly identified.");
    }

    @Test
    void testHeapBaseMinAddress() {
        String opts = "-Xms1g -XX:HeapBaseMinAddress=12g -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:HeapBaseMinAddress=12g", jvmOptions.getHeapBaseMinAddress(),
                "HeapBaseMinAddress not correct.");
    }

    @Test
    void testIgnoreUnrecognizedVmOptions() {
        String opts = "-XX:+IgnoreUnrecognizedVMOptions";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+IgnoreUnrecognizedVMOptions", jvmOptions.getIgnoreUnrecognizedVmOptions(),
                "IgnoreUnrecognizedVMOptions not correct.");
    }

    @Test
    void testInitialBootClassLoaderMetaspaceSize() {
        String opts = "-Xms1g -XX:InitialBootClassLoaderMetaspaceSize=8m -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:InitialBootClassLoaderMetaspaceSize=8m", jvmOptions.getInitialBootClassLoaderMetaspaceSize(),
                "InitialBootClassLoaderMetaspaceSize not correct.");
    }

    @Test
    void testInitialCodeCacheSize() {
        String opts = "-Xms1g -XX:InitialCodeCacheSize=32m -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:InitialCodeCacheSize=32m", jvmOptions.getInitialCodeCacheSize(),
                "-XX:InitialCodeCacheSize not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testInitialRAMPercentage() {
        String opts = "-Xms1g -XX:InitialRAMPercentage=25.0 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:InitialRAMPercentage=25.0", jvmOptions.getInitialRAMPercentage(),
                "InitialRAMPercentage not correct.");
    }

    @Test
    void testInitiatingHeapOccupancyPercent() {
        String opts = "-Xms1g -XX:InitiatingHeapOccupancyPercent=1 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:InitiatingHeapOccupancyPercent=1", jvmOptions.getInitiatingHeapOccupancyPercent(),
                "InitiatingHeapOccupancyPercent not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testLog() {
        String opts = "-Xmx1g -Xlog:gc*:file=/path/to/gc.log:time,uptimemillis:filecount=5,filesize=3M";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xlog:gc*:file=/path/to/gc.log:time,uptimemillis:filecount=5,filesize=3M",
                jvmOptions.getLog().get(0), "-Xlog not correct.");
    }

    @Test
    void testLoopStripMiningIter() {
        String opts = "-Xms1000m -XX:LoopStripMiningIter=1000 -Xmx1500m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:LoopStripMiningIter=1000", jvmOptions.getLoopStripMiningIter(),
                "LoopStripMiningIter not correct.");
    }

    @Test
    void testMarkStackSize() {
        String opts = "-Xms1g -XX:MarkStackSize=4194304 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MarkStackSize=4194304", jvmOptions.getMarkStackSize(), "MarkStackSize not correct.");
    }

    @Test
    void testMarkStackSizeMax() {
        String opts = "-Xmx1g -XX:MarkStackSizeMax=2147483646";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MarkStackSizeMax=2147483646", jvmOptions.getMarkStackSizeMax(),
                "MarkStackSizeMax not correct.");
    }

    @Test
    void testMaxFdLimit() {
        String opts = "-Xms1g -XX:+MaxFDLimit -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+MaxFDLimit", jvmOptions.getMaxFdLimit(), "MaxFDLimit not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testMaxInlinLevel() {
        String opts = "-Xms1g -XX:MaxInlineLevel=15 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MaxInlineLevel=15", jvmOptions.getMaxInlineLevel(), "MaxInlineLevel not correct.");
    }

    @Test
    void testMaxjitcodesizeBigG() {
        String opts = "-Xms1g -Xmaxjitcodesize1G -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xmaxjitcodesize1G", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxjitcodesizeBigK() {
        String opts = "-Xms1g -Xmaxjitcodesize4096K -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xmaxjitcodesize4096K", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxjitcodesizeBigM() {
        String opts = "-Xms1g -Xmaxjitcodesize1024M -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xmaxjitcodesize1024M", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxjitcodesizeNoUnits() {
        String opts = "-Xms1g -Xmaxjitcodesize4096000 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xmaxjitcodesize4096000", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxjitcodesizeSmallG() {
        String opts = "-Xms1g -Xmaxjitcodesize1g -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xmaxjitcodesize1g", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxjitcodesizeSmallK() {
        String opts = "-Xms1g -Xmaxjitcodesize4096k -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xmaxjitcodesize4096k", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxjitcodesizeSmallM() {
        String opts = "-Xms1g -Xmaxjitcodesize1024m -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xmaxjitcodesize1024m", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxMetaspaceFreeRatio() {
        String opts = "-Xms1g -XX:MaxMetaspaceFreeRatio=80 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MaxMetaspaceFreeRatio=80", jvmOptions.getMaxMetaspaceFreeRatio(),
                "MaxMetaspaceFreeRatio not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testMaxNewSize() {
        String opts = "-XX:MaxNewSize=512m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MaxNewSize=512m", jvmOptions.getMaxNewSize(), "MaxNewSize not correct.");
    }

    @Test
    void testMaxRam() {
        String opts = "-Xms1g -XX:MaxRAM=2g -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MaxRAM=2g", jvmOptions.getMaxRAM(), "MaxRAM not correct.");
    }

    @Test
    void testMaxRAMPercentage() {
        String opts = "-Xms1g -XX:MaxRAMPercentage=80.0 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MaxRAMPercentage=80.0", jvmOptions.getMaxRAMPercentage(), "MaxRAMPercentage not correct.");
    }

    @Test
    void testMaxRAMPercentageJdk11() {
        String opts = "-Xms1g -XX:MaxRAMPercentage=80 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MaxRAMPercentage=80", jvmOptions.getMaxRAMPercentage(), "MaxRAMPercentage not correct.");
    }

    @Test
    void testMinHeapDeltaBytes() {
        String opts = "-Xmx1g -XX:MinHeapDeltaBytes=123456";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MinHeapDeltaBytes=123456", jvmOptions.getMinHeapDeltaBytes(),
                "MinHeapDeltaBytes not correct.");
    }

    @Test
    void testMinMetaspaceFreeRatio() {
        String opts = "-Xms1g -XX:MinMetaspaceFreeRatio=50 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MinMetaspaceFreeRatio=50", jvmOptions.getMinMetaspaceFreeRatio(),
                "MinMetaspaceFreeRatio not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testMinRAMPercentage() {
        String opts = "-Xms1g -XX:MinRAMPercentage=50.0 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MinRAMPercentage=50.0", jvmOptions.getMinRAMPercentage(), "MinRAMPercentage not correct.");
    }

    @Test
    void testMinRAMPercentageJdk11() {
        String opts = "-Xms1g -XX:MinRAMPercentage=50 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:MinRAMPercentage=50", jvmOptions.getMinRAMPercentage(), "MinRAMPercentage not correct.");
    }

    @Test
    void testMultipleAddExports() {
        String opts = "-Xms1g --add-exports=java.base/sun.nio.ch=ALL-UNNAMED "
                + "--add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED "
                + "--add-exports=jdk.unsupported/sun.reflect=ALL-UNNAMED -Xmx2g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertNull(jvmOptions.getDuplicates(), "Duplicates not correct.");
    }

    @Test
    void testMultipleLog() {
        String opts = "-Xms1g -Xlog:gc*=info:stdout:time,level,tags -Xlog:gc*=info:stdout:time,level,tags";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xlog:gc*=info:stdout:time,level,tags -Xlog:gc*=info:stdout:time,level,tags",
                jvmOptions.getDuplicates(), "Duplicates not correct.");
    }

    @Test
    void testMultipleLogNo() {
        String opts = "-Xms1g -Xlog:gc*=info:stdout:time,level,tags -Xlog:gc*=warn:stdout:time,level,tags";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertNull(jvmOptions.getDuplicates(), "Duplicates not correct.");
    }

    @Test
    void testNativeMemoryTracking() {
        String opts = "-Xms1g -XX:NativeMemoryTracking=detail -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:NativeMemoryTracking=detail", jvmOptions.getNativeMemoryTracking(),
                "NativeMemoryTracking not correct.");
    }

    @Test
    void testNewRatio() {
        String opts = "-XX:NewRatio=3";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:NewRatio=3", jvmOptions.getNewRatio(), "NewRatio not correct.");
    }

    @Test
    void testNonNMethodCodeHeapSize() {
        String opts = "-Xms1g -XX:NonNMethodCodeHeapSize=5825164 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:NonNMethodCodeHeapSize=5825164", jvmOptions.getNonNMethodCodeHeapSize(),
                "NonNMethodCodeHeapSize not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testNonProfiledCodeHeapSize() {
        String opts = "-Xms1g -XX:NonProfiledCodeHeapSize=122916538 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:NonProfiledCodeHeapSize=122916538", jvmOptions.getNonProfiledCodeHeapSize(),
                "NonProfiledCodeHeapSize not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testOldPlabSize() {
        String opts = "-Xms1g -XX:OldPLABSize=16 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:OldPLABSize=16", jvmOptions.getOldPlabSize(), "OldPLABSize not correct.");
    }

    @Test
    void testOldSize() {
        String opts = "-XX:InitialHeapSize=4370464768 -XX:MaxHeapSize=10737418240 -XX:OldSize=2913992704";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:OldSize=2913992704", jvmOptions.getOldSize(), "OldSize not correct.");
    }

    @Test
    void testOmitStackTraceInFastThrow() {
        String opts = "-Xms1g -XX:-OmitStackTraceInFastThrow -Xmx2g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:-OmitStackTraceInFastThrow", jvmOptions.getOmitStackTraceInFastThrow(),
                "OmitStackTraceInFastThrow not correct.");
        assertNull(jvmOptions.getUnaccountedDisabledOptions(), "Unaccounted disabled options incorrect.");
    }

    @Test
    void testOnOutOfMemoryErrorKill9() {
        String opts = "-Xms1g -XX:OnOutOfMemoryError=kill -9 %p -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:OnOutOfMemoryError=kill -9 %p", jvmOptions.getOnOutOfMemoryError(),
                "OnOutOfMemoryError not correct.");
    }

    @Test
    void testOnOutOfMemoryErrorKillAbrt() {
        String opts = "-Xms1g -XX:OnOutOfMemoryError=/bin/kill -ABRT %p -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:OnOutOfMemoryError=/bin/kill -ABRT %p", jvmOptions.getOnOutOfMemoryError(),
                "OnOutOfMemoryError not correct.");
    }

    @Test
    void testOnOutOfMemoryScript() {
        String opts = "-Xms1g -XX:OnOutOfMemoryError=\"/usr/bin/restart_tomcat\" -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:OnOutOfMemoryError=\"/usr/bin/restart_tomcat\"", jvmOptions.getOnOutOfMemoryError(),
                "OnOutOfMemoryError not correct.");
    }

    @Test
    void testPerMethodRecompilationCutoff() {
        String opts = "-Xmx1g -XX:PerMethodRecompilationCutoff=10000 -XX:G1NewSizePercent=1";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:PerMethodRecompilationCutoff=10000", jvmOptions.getPerMethodRecompilationCutoff(),
                "PerMethodRecompilationCutoff not correct.");
    }

    @Test
    void testPrintCodeCache() {
        String opts = "-Xms1g -XX:+PrintCodeCache -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+PrintCodeCache", jvmOptions.getPrintCodeCache(), "PrintCodeCache not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testPrintCommandLineFlags() {
        String opts = "-Xms1g -XX:+PrintCommandLineFlags -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+PrintCommandLineFlags", jvmOptions.getPrintCommandLineFlags(),
                "PrintCommandLineFlags not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testPrintConcurrentLocks() {
        String opts = "-Xms1g -XX:+PrintConcurrentLocks -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+PrintConcurrentLocks", jvmOptions.getPrintConcurrentLocks(),
                "PrintConcurrentLocks not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testPrintGCCause() {
        String opts = "-Xms1g -XX:+PrintGCCause -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+PrintGCCause", jvmOptions.getPrintGcCause(), "PrintGCCause not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testPrintNMTStatistics() {
        String opts = "-Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+PrintNMTStatistics";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+PrintNMTStatistics", jvmOptions.getPrintNMTStatistics(), "PrintNMTStatistics not correct.");
    }

    @Test
    void testPrintStringTableStatistics() {
        String opts = "-Xms1g -XX:+PrintStringTableStatistics -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+PrintStringTableStatistics", jvmOptions.getPrintStringTableStatistics(),
                "PrintStringTableStatistics not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testProfiledCodeHeapSize() {
        String opts = "-Xms1g -XX:ProfiledCodeHeapSize=122916538 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:ProfiledCodeHeapSize=122916538", jvmOptions.getProfiledCodeHeapSize(),
                "ProfiledCodeHeapSize not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testResizePlab() {
        String opts = "-Xms1g -XX:-ResizePLAB -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:-ResizePLAB", jvmOptions.getResizePlab(), "ResizePLAB not correct.");
    }

    @Test
    void testResizeTLAB() {
        String opts = "-Xms1g -XX:+ResizeTLAB -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+ResizeTLAB", jvmOptions.getResizeTlab(), "ResizeTLAB not correct.");
    }

    @Test
    void testSegmentedCodeCache() {
        String opts = "-Xms1g -XX:-SegmentedCodeCache -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:-SegmentedCodeCache", jvmOptions.getSegmentedCodeCache(), "SegmentedCodeCache not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testServerNoverify() {
        String opts = "-Xmx1g -server -noverify";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertTrue(jvmOptions.isServer(), "server not correct.");
        assertTrue(jvmOptions.isNoverify(), "noverify not correct.");
    }

    @Test
    void testShenandoahGuaranteedGCInterval() {
        String opts = "-Xms1g -XX:ShenandoahGuaranteedGCInterval=20000 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:ShenandoahGuaranteedGCInterval=20000", jvmOptions.getShenandoahGuaranteedGCInterval(),
                "ShenandoahGuaranteedGCInterval not correct.");
    }

    @Test
    void testShenandoahSoftMaxHeapSize() {
        String opts = "-Xms1g -XX:ShenandoahSoftMaxHeapSize=4294967296 -Xmx5g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:ShenandoahSoftMaxHeapSize=4294967296", jvmOptions.getShenandoahSoftMaxHeapSize(),
                "ShenandoahSoftMaxHeapSize not correct.");
    }

    @Test
    void testShenandoahUncommitDelay() {
        String opts = "-Xms1g -XX:ShenandoahUncommitDelay=5000 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:ShenandoahUncommitDelay=5000", jvmOptions.getShenandoahUncommitDelay(),
                "ShenandoahUncommitDelay not correct.");
    }

    @Test
    void testSoftMaxHeapSize() {
        String opts = "-Xms1g -XX:SoftMaxHeapSize=4294967296 -Xmx5g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:SoftMaxHeapSize=4294967296", jvmOptions.getSoftMaxHeapSize(), "SoftMaxHeapSize not correct.");
    }

    @Test
    void testSoftRefLRUPolicyMSPerMB() {
        String opts = "-Xms1000m -XX:SoftRefLRUPolicyMSPerMB=10 -Xmx1500m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:SoftRefLRUPolicyMSPerMB=10", jvmOptions.getSoftRefLRUPolicyMSPerMB(),
                "SoftRefLRUPolicyMSPerMB not correct.");
    }

    @Test
    void testStartFlightRecordingColon() {
        String opts = "-Xms1g -XX:StartFlightRecording:filename=recording.jfr,dumponexit=true,settings=default.jfc"
                + " -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:StartFlightRecording:filename=recording.jfr,dumponexit=true,settings=default.jfc",
                jvmOptions.getStartFlightRecording(), "StartFlightRecording not correct.");
    }

    @Test
    void testStartFlightRecordingEqualSign() {
        String opts = "-Xms1g -XX:StartFlightRecording=duration=200s,filename=flight.jfr -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:StartFlightRecording=duration=200s,filename=flight.jfr", jvmOptions.getStartFlightRecording(),
                "StartFlightRecording not correct.");
    }

    @Test
    void testStringTableSize() {
        String opts = "-Xms1g -XX:StringTableSize=123456 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:StringTableSize=123456", jvmOptions.getStringTableSize(), "StringTableSize not correct.");
    }

    @Test
    void testSystemProperties() {
        String opts = "-Xmx1500m -Xms1000m -Dcatalina.base=/path/to/tomcat -Xss512k";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals(1, jvmOptions.getSystemProperties().size(), "System properties count not correct.");
        assertEquals("-Dcatalina.base=/path/to/tomcat", jvmOptions.getSystemProperties().get(0),
                "System property not correct.");
        assertEquals(3, jvmOptions.getOptions().size(), "JVM options count not correct.");
    }

    @Test
    void testTraceClassLoading() {
        String opts = "-Xmx1500m -Xms1000m -XX:-TraceClassLoading -Xss512k";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:-TraceClassLoading", jvmOptions.getTraceClassLoading(), "TraceClassLoading not correct.");
    }

    @Test
    void testTraceClassUnloading() {
        String opts = "-Xmx1500m -Xms1000m -XX:-TraceClassUnloading -Xss512k";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:-TraceClassUnloading", jvmOptions.getTraceClassUnloading(),
                "TraceClassUnloading not correct.");
    }

    @Test
    void testUnknownOptions() {
        String opts = "-Xms1g -XX:+ParallelRefProcEnabled -XX:+UseTLAB -XX:-UseGCOverheadLimit "
                + "-XX:-UseSplitVerifier -XX:CMSIncrementalSafetyFactor=20 -XX:Tier2CompileThreshold=2000 "
                + "-XX:Tier3CompileThreshold=2000 -XX:Tier4CompileThreshold=15000 -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals(0, jvmOptions.getUndefined().size(), "Unknown options found.");
    }

    @Test
    void testUseAvx() {
        String opts = "-Xms1000m -XX:UseAVX=0 -Xmx1500m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:UseAVX=0", jvmOptions.getUseAvx(), "UseAVX not correct.");
    }

    @Test
    void testUseCmsCompactAtFullCollection() {
        String opts = "-Xms1g -XX:+UseCMSCompactAtFullCollection -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+UseCMSCompactAtFullCollection", jvmOptions.getUseCmsCompactAtFullCollection(),
                "UseCMSCompactAtFullCollection not correct.");
    }

    @Test
    void testUseCodeCacheFlushing() {
        String opts = "-Xmx1g -XX:+UseCodeCacheFlushing";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+UseCodeCacheFlushing", jvmOptions.getUseCodeCacheFlushing(),
                "UseCodeCacheFlushing not correct.");
    }

    @Test
    void testUseContainerSupport() {
        String opts = "-Xms1g -XX:+UseContainerSupport -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+UseContainerSupport", jvmOptions.getUseContainerSupport(),
                "UseContainerSupport not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testUseCountedLoopSafepoints() {
        String opts = "-Xms1000m -XX:+UseCountedLoopSafepoints -Xmx1500m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+UseCountedLoopSafepoints", jvmOptions.getUseCountedLoopSafepoints(),
                "UseCountedLoopSafepoints not correct.");
    }

    @Test
    void testUseDynamicNumberOfCompilerThreads() {
        String opts = "-Xms1g -XX:+UseDynamicNumberOfCompilerThreads -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+UseDynamicNumberOfCompilerThreads", jvmOptions.getUseDynamicNumberOfCompilerThreads(),
                "UseDynamicNumberOfCompilerThreads not correct.");
    }

    @Test
    void testUseDynamicNumberOfGcThreads() {
        String opts = "-Xms1g -XX:+UseDynamicNumberOfGCThreads -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+UseDynamicNumberOfGCThreads", jvmOptions.getUseDynamicNumberOfGcThreads(),
                "UseDynamicNumberOfGCThreads not correct.");
    }

    @Test
    void testUseLargePagesIndividualAllocation() {
        String opts = "-Xms1g -XX:-UseLargePagesIndividualAllocation -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:-UseLargePagesIndividualAllocation", jvmOptions.getUseLargePagesIndividualAllocation(),
                "UseLargePagesIndividualAllocation not correct.");
    }

    @Test
    void testUseLargePagesInMetaspace() {
        String opts = "-Xms1g -XX:+UseLargePagesInMetaspace -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+UseLargePagesInMetaspace", jvmOptions.getUseLargePagesInMetaspace(),
                "UseLargePagesInMetaspace not correct.");
    }

    @Test
    void testUseNUMAInterleaving() {
        String opts = "-Xms1g -XX:-UseNUMAInterleaving -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:-UseNUMAInterleaving", jvmOptions.getUseNUMAInterleaving(),
                "UseNUMAInterleaving not correct.");
    }

    @Test
    void testUseThreadPriorities() {
        String opts = "-Xms1g -XX:+UseThreadPriorities -Xmx1g";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+UseThreadPriorities", jvmOptions.getUseThreadPriorities(),
                "UseThreadPriorities not correct.");
    }

    @Test
    void testUseTransparentHugePages() {
        String opts = "-Xms1000m -XX:+UseTransparentHugePages -XX:+CMSIncrementalMode -Xmx1500m";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:+UseTransparentHugePages", jvmOptions.getUseTransparentHugePages(),
                "UseTransparentHugePages not correct.");
    }

    @Test
    void testVerify() {
        String opts = "-Xverify:none";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-Xverify:none", jvmOptions.getVerify(), "Verify not correct.");
    }

    @Test
    void testZUncommitDelay() {
        String opts = "-XX:+UseZGC-Xms1g -XX:SoftMaxHeapSize=4294967296 -Xmx5g -XX:ZUncommitDelay=240";
        JvmContext context = new JvmContext(opts);
        JvmOptions jvmOptions = new JvmOptions(context);
        assertEquals("-XX:ZUncommitDelay=240", jvmOptions.getZUncommitDelay(), "ZUncommitDelay not correct.");
    }
}
