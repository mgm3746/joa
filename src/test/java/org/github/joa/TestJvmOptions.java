/**********************************************************************************************************************
 * JVM Options Analyzer                                                                                               *
 *                                                                                                                    *
 * Copyright (c) 2022 Mike Millson                                                                                    *
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.github.joa.domain.GarbageCollector;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class TestJvmOptions {
    @Test
    void test7NewOptions() {
        String jvmArgs = "-XX:MaxJavaStackTraceDepth=50000 -XX:MaxGCPauseMillis=500 -XX:G1HeapRegionSize=4m "
                + "-XX:+UseStringDeduplication -XX:OnOutOfMemoryError=\"pmap %p\"  -XX:+DebugNonSafepoints "
                + "-XX:FlightRecorderOptions=stackdepth=256";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
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
    void testAddExports() {
        String jvmArgs = "-Xmx1g --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("--add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED", jvmOptions.getAddExports().get(0),
                "--add-exports not correct.");
    }

    @Test
    void testAgentLib() {
        String jvmArgs = "-Xms1g -agentlib:am_sun_16=/path/to/my.properties -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-agentlib:am_sun_16=/path/to/my.properties", jvmOptions.getAgentlib().get(0),
                "JDPA socket transport (debugging) not correct.");
    }

    @Test
    void testClasspath() {
        String jvmArgs = "-Xmx1g -classpath /path/to/tomcat/bin/bootstrap.jar:/path/to/tomcat/bin/tomcat-juli.jar:"
                + "/path/to/java/ant.jar:/path/to/java/ant-launcher.jar:/path/to/java/lib/tools.jar -Xss512k";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(0, jvmOptions.getUndefined().size(), "Unknown options found.");
        assertEquals(
                "-classpath /path/to/tomcat/bin/bootstrap.jar:/path/to/tomcat/bin/tomcat-juli.jar:"
                        + "/path/to/java/ant.jar:/path/to/java/ant-launcher.jar:/path/to/java/lib/tools.jar",
                jvmOptions.getClasspath(), "classpath not correct.");
    }

    @Test
    void testCMSScavengeBeforeRemark() {
        String jvmArgs = "-Xms1g -XX:+CMSScavengeBeforeRemark -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:+CMSScavengeBeforeRemark", jvmOptions.getCmsScavengeBeforeRemark(),
                "CMSScavengeBeforeRemark not correct.");
    }

    @Test
    void testCncurrentio() {
        String jvmArgs = "-Xms1g -Xconcurrentio -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertTrue(jvmOptions.isConcurrentio(), "concurrentio not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testCommonOptions() {
        String jvmArgs = "-Xmx1500m -Xms1000m -Xss512k -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=2048m";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-Xss512k", jvmOptions.getThreadStackSize(), "Thread stack size not correct.");
        assertEquals("-Xms1000m", jvmOptions.getInitialHeapSize(), "Initial heap size not correct.");
        assertEquals("-Xmx1500m", jvmOptions.getMaxHeapSize(), "Max heap size not correct.");
        assertEquals("-XX:MetaspaceSize=256M", jvmOptions.getMetaspaceSize(), "Metaspace size not correct.");
        assertEquals("-XX:MaxMetaspaceSize=2048m", jvmOptions.getMaxMetaspaceSize(), "Max metaspace size not correct.");
    }

    @Test
    void testCompileThreshold() {
        String jvmArgs = "-Xms1g -XX:CompileThreshold=5000 -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:CompileThreshold=5000", jvmOptions.getCompileThreshold(), "CompileThreshold not correct.");
    }

    @Test
    void testCrashOnOutOfMemoryError() {
        String jvmArgs = "-Xms1g -XX:+CrashOnOutOfMemoryError -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:+CrashOnOutOfMemoryError", jvmOptions.getCrashOnOutOfMemoryError(),
                "CrashOnOutOfMemoryError not correct.");
    }

    @Test
    void testDisableAttachMechanism() {
        String jvmArgs = "-XX:+DisableAttachMechanism";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:+DisableAttachMechanism", jvmOptions.getDisableAttachMechanism(),
                "DisableAttachMechanism not correct.");
    }

    @Test
    void testDisableJvmSignalHandling() {
        String jvmArgs = "-Xms1g -Xrs -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertTrue(jvmOptions.isRs(), "rs not correct.");
        assertEquals(0, jvmOptions.getUndefined().size(), "Undefined options not correct.");
    }

    @Test
    void testDuplicateAddExports() {
        String jvmArgs = "-Xms1g --add-exports=java.base/sun.nio.ch=ALL-UNNAMED "
                + "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED -Xmx2g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("--add-exports=java.base/sun.nio.ch=ALL-UNNAMED --add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
                jvmOptions.getDuplicates(), "Duplicates not correct.");
    }

    @Test
    void testDuplicateBootclasspath() {
        String jvmArgs = "-Xms1g -Xbootclasspath/p:/path/to/jar1 -Xbootclasspath/p:/path/to/jar2 -Xmx2g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertNull(jvmOptions.getDuplicates(), "Duplicates not correct.");
    }

    @Test
    void testDuplicateXms() {
        String jvmArgs = "-Xms1g -Xms2g -Xmaxjitcodesize1G -Xmx2g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-Xms1g -Xms2g", jvmOptions.getDuplicates(), "Duplicates not correct.");
    }

    @Test
    void testDuplicateXmxMaxHeap() {
        String jvmArgs = "-XX:MaxHeapSize=2048m -Xmx4096m";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:MaxHeapSize=2048m -Xmx4096m", jvmOptions.getDuplicates(), "Duplicates not correct.");
    }

    @Test
    void testG1ConcRefinementThreads() {
        String jvmArgs = "-Xmx1500m -Xms1000m -XX:G1ConcRefinementThreads=4";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:G1ConcRefinementThreads=4", jvmOptions.getG1ConcRefinementThreads(),
                "G1ConcRefinementThreads not correct.");
    }

    @Test
    void testG1ReservePercent() {
        String jvmArgs = "-Xms1g -XX:G1ReservePercent=10 -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:G1ReservePercent=10", jvmOptions.getG1ReservePercent(), "G1ReservePercent not correct.");
    }

    @Test
    void testGarbageCollectorG1() {
        String jvmArgs = "-Xmx1500m -Xms1000m -XX:+UseG1GC";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(1, jvmOptions.getGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.G1),
                GarbageCollector.G1 + " collector not identified.");
    }

    @Test
    void testGarbageCollectorShenandoah() {
        String jvmArgs = "-Xmx1500m -Xms1000m -XX:+UseShenandoahGC";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(1, jvmOptions.getGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.SHENANDOAH),
                GarbageCollector.G1 + " collector not identified.");
    }

    @Test
    void testGarbageCollectorsParallelScavengeParallelOld() {
        String jvmArgs = "-Xmx1500m -Xms1000m -XX:+UseParallelGC";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(2, jvmOptions.getGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.PARALLEL_SCAVENGE),
                GarbageCollector.PARALLEL_SCAVENGE + " collector not identified.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.PARALLEL_OLD),
                GarbageCollector.PARALLEL_OLD + " collector not identified.");
        jvmArgs = "-Xmx1500m -Xms1000m -XX:+UseParallelOldGC";
        jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(2, jvmOptions.getGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.PARALLEL_SCAVENGE),
                GarbageCollector.PARALLEL_SCAVENGE + " collector not identified.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.PARALLEL_OLD),
                GarbageCollector.PARALLEL_OLD + " collector not identified.");
        jvmArgs = "-Xmx1500m -Xms1000m -XX:+UseParallelGC -XX:+UseParallelOldGC";
        jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(2, jvmOptions.getGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.PARALLEL_SCAVENGE),
                GarbageCollector.PARALLEL_SCAVENGE + " collector not identified.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.PARALLEL_OLD),
                GarbageCollector.PARALLEL_OLD + " collector not identified.");
    }

    @Test
    void testGarbageCollectorsParallelScavengeSerialOld() {
        String jvmArgs = "-Xmx1500m -Xms1000m -XX:+UseParallelGC -XX:-UseParallelOldGC";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(2, jvmOptions.getGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.PARALLEL_SCAVENGE),
                GarbageCollector.PARALLEL_SCAVENGE + " collector not identified.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.SERIAL_OLD),
                GarbageCollector.SERIAL_OLD + " collector not identified.");
    }

    @Test
    void testGarbageCollectorsParNewConcurrentMarkSweep() {
        String jvmArgs = "-Xmx1500m -Xms1000m -XX:+UseConcMarkSweepGC";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(2, jvmOptions.getGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.PAR_NEW),
                GarbageCollector.PAR_NEW + " collector not identified.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.CMS),
                GarbageCollector.CMS + " collector not identified.");
        jvmArgs = "-Xmx1500m -Xms1000m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC";
        jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(2, jvmOptions.getGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.PAR_NEW),
                GarbageCollector.PAR_NEW + " collector not identified.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.CMS),
                GarbageCollector.CMS + " collector not identified.");
    }

    @Test
    void testGarbageCollectorsParNewSerialOld() {
        String jvmArgs = "-Xmx1500m -Xms1000m -XX:+UseParNewGC";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(2, jvmOptions.getGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.PAR_NEW),
                GarbageCollector.PAR_NEW + " collector not identified.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.SERIAL_OLD),
                GarbageCollector.SERIAL_OLD + " collector not identified.");
    }

    @Test
    void testGarbageCollectorsSerialSerialOld() {
        String jvmArgs = "-Xmx1500m -Xms1000m -XX:+UseSerialGC";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(2, jvmOptions.getGarbageCollectors().size(), "Number of garbage collector not correct.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.SERIAL),
                GarbageCollector.SERIAL + " collector not identified.");
        assertTrue(jvmOptions.getGarbageCollectors().contains(GarbageCollector.SERIAL_OLD),
                GarbageCollector.SERIAL_OLD + " collector not identified.");
    }

    @Test
    void testGcLoggingOptions() {
        String jvmArgs = "-Xmx1500m -Xms1000m -verbose:gc -Xloggc:/path/to/EAP-7.1.0/standalone/log/gc.log "
                + "-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 "
                + "-XX:GCLogFileSize=3M -Xss512k";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
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
    void testHeapBaseMinAddress() {
        String jvmArgs = "-Xms1g -XX:HeapBaseMinAddress=12g -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:HeapBaseMinAddress=12g", jvmOptions.getHeapBaseMinAddress(),
                "HeapBaseMinAddress not correct.");
    }

    @Test
    void testIgnoreUnrecognizedVmOptions() {
        String jvmArgs = "-XX:+IgnoreUnrecognizedVMOptions";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:+IgnoreUnrecognizedVMOptions", jvmOptions.getIgnoreUnrecognizedVmOptions(),
                "IgnoreUnrecognizedVMOptions not correct.");
    }

    @Test
    void testLog() {
        String jvmArgs = "-Xmx1g -Xlog:gc*:file=/path/to/gc.log:time,uptimemillis:filecount=5,filesize=3M";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-Xlog:gc*:file=/path/to/gc.log:time,uptimemillis:filecount=5,filesize=3M",
                jvmOptions.getLog().get(0), "-Xlog not correct.");
    }

    @Test
    void testMaxInlinLevel() {
        String jvmArgs = "-Xms1g -XX:MaxInlineLevel=15 -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:MaxInlineLevel=15", jvmOptions.getMaxInlineLevel(), "MaxInlineLevel not correct.");
    }

    @Test
    void testMaxjitcodesizeBigG() {
        String jvmArgs = "-Xms1g -Xmaxjitcodesize1G -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-Xmaxjitcodesize1G", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxjitcodesizeBigK() {
        String jvmArgs = "-Xms1g -Xmaxjitcodesize4096K -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-Xmaxjitcodesize4096K", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxjitcodesizeBigM() {
        String jvmArgs = "-Xms1g -Xmaxjitcodesize1024M -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-Xmaxjitcodesize1024M", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxjitcodesizeNoUnits() {
        String jvmArgs = "-Xms1g -Xmaxjitcodesize4096000 -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-Xmaxjitcodesize4096000", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxjitcodesizeSmallG() {
        String jvmArgs = "-Xms1g -Xmaxjitcodesize1g -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-Xmaxjitcodesize1g", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxjitcodesizeSmallK() {
        String jvmArgs = "-Xms1g -Xmaxjitcodesize4096k -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-Xmaxjitcodesize4096k", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxjitcodesizeSmallM() {
        String jvmArgs = "-Xms1g -Xmaxjitcodesize1024m -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-Xmaxjitcodesize1024m", jvmOptions.getMaxjitcodesize(), "maxjitcodesize not correct.");
    }

    @Test
    void testMaxNewSize() {
        String jvmArgs = "-XX:MaxNewSize=512m";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:MaxNewSize=512m", jvmOptions.getMaxNewSize(), "MaxNewSize not correct.");
    }

    @Test
    void testMinHeapDeltaBytes() {
        String jvmArgs = "-Xmx1g -XX:MinHeapDeltaBytes=123456";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:MinHeapDeltaBytes=123456", jvmOptions.getMinHeapDeltaBytes(),
                "MinHeapDeltaBytes not correct.");
    }

    @Test
    void testMultipleAddExports() {
        String jvmArgs = "-Xms1g --add-exports=java.base/sun.nio.ch=ALL-UNNAMED "
                + "--add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED "
                + "--add-exports=jdk.unsupported/sun.reflect=ALL-UNNAMED -Xmx2g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertNull(jvmOptions.getDuplicates(), "Duplicates not correct.");
    }

    @Test
    void testNativeMemoryTracking() {
        String jvmArgs = "-Xms1g -XX:NativeMemoryTracking=detail -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:NativeMemoryTracking=detail", jvmOptions.getNativeMemoryTracking(),
                "NativeMemoryTracking not correct.");
    }

    @Test
    void testNewRatio() {
        String jvmArgs = "-XX:NewRatio=3";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:NewRatio=3", jvmOptions.getNewRatio(), "NewRatio not correct.");
    }

    @Test
    void testOnOutOfMemoryErrorKill9() {
        String jvmArgs = "-Xms1g -XX:OnOutOfMemoryError=kill -9 %p -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:OnOutOfMemoryError=kill -9 %p", jvmOptions.getOnOutOfMemoryError(),
                "OnOutOfMemoryError not correct.");
    }

    @Test
    void testOnOutOfMemoryErrorKillAbrt() {
        String jvmArgs = "-Xms1g -XX:OnOutOfMemoryError=/bin/kill -ABRT %p -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:OnOutOfMemoryError=/bin/kill -ABRT %p", jvmOptions.getOnOutOfMemoryError(),
                "OnOutOfMemoryError not correct.");
    }

    @Test
    void testOnOutOfMemoryScript() {
        String jvmArgs = "-Xms1g -XX:OnOutOfMemoryError=\"/usr/bin/restart_tomcat\" -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:OnOutOfMemoryError=\"/usr/bin/restart_tomcat\"", jvmOptions.getOnOutOfMemoryError(),
                "OnOutOfMemoryError not correct.");
    }

    @Test
    void testResizePlab() {
        String jvmArgs = "-Xms1g -XX:-ResizePLAB -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:-ResizePLAB", jvmOptions.getResizePlab(), "ResizePLAB not correct.");
    }

    @Test
    void testServerNoverify() {
        String jvmArgs = "-Xmx1g -server -noverify";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertTrue(jvmOptions.isServer(), "server not correct.");
        assertTrue(jvmOptions.isNoverify(), "noverify not correct.");
    }

    @Test
    void testShenandoahGuaranteedGCInterval() {
        String jvmArgs = "-Xms1g -XX:ShenandoahGuaranteedGCInterval=20000 -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:ShenandoahGuaranteedGCInterval=20000", jvmOptions.getShenandoahGuaranteedGCInterval(),
                "ShenandoahGuaranteedGCInterval not correct.");
    }

    @Test
    void testShenandoahUncommitDelay() {
        String jvmArgs = "-Xms1g -XX:ShenandoahUncommitDelay=5000 -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:ShenandoahUncommitDelay=5000", jvmOptions.getShenandoahUncommitDelay(),
                "ShenandoahUncommitDelay not correct.");
    }

    @Test
    void testSystemProperties() {
        String jvmArgs = "-Xmx1500m -Xms1000m -Dcatalina.base=/path/to/tomcat -Xss512k";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(1, jvmOptions.getSystemProperties().size(), "System properties count not correct.");
        assertEquals("-Dcatalina.base=/path/to/tomcat", jvmOptions.getSystemProperties().get(0),
                "System property not correct.");
    }

    @Test
    void testTraceClassLoading() {
        String jvmArgs = "-Xmx1500m -Xms1000m -XX:-TraceClassLoading -Xss512k";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:-TraceClassLoading", jvmOptions.getTraceClassLoading(), "TraceClassLoading not correct.");
    }

    @Test
    void testTraceClassUnloading() {
        String jvmArgs = "-Xmx1500m -Xms1000m -XX:-TraceClassUnloading -Xss512k";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:-TraceClassUnloading", jvmOptions.getTraceClassUnloading(),
                "TraceClassUnloading not correct.");
    }

    @Test
    void testUnknownOptions() {
        String jvmArgs = "-Xms1g -XX:+ParallelRefProcEnabled -XX:+UseTLAB -XX:-UseGCOverheadLimit "
                + "-XX:-UseSplitVerifier -XX:CMSIncrementalSafetyFactor=20 -XX:Tier2CompileThreshold=2000 "
                + "-XX:Tier3CompileThreshold=2000 -XX:Tier4CompileThreshold=15000 -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals(0, jvmOptions.getUndefined().size(), "Unknown options found.");
    }

    @Test
    void testUseAvx() {
        String jvmArgs = "-Xms1000m -XX:UseAVX=0 -Xmx1500m";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:UseAVX=0", jvmOptions.getUseAvx(), "UseAVX not correct.");
    }

    @Test
    void testUseCodeCacheFlushing() {
        String jvmArgs = "-Xmx1g -XX:+UseCodeCacheFlushing";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:+UseCodeCacheFlushing", jvmOptions.getUseCodeCacheFlushing(),
                "UseCodeCacheFlushing not correct.");
    }

    @Test
    void testUseDynamicNumberOfCompilerThreads() {
        String jvmArgs = "-Xms1g -XX:+UseDynamicNumberOfCompilerThreads -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:+UseDynamicNumberOfCompilerThreads", jvmOptions.getUseDynamicNumberOfCompilerThreads(),
                "UseDynamicNumberOfCompilerThreads not correct.");
    }

    @Test
    void testUseDynamicNumberOfGcThreads() {
        String jvmArgs = "-Xms1g -XX:+UseDynamicNumberOfGCThreads -Xmx1g";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-XX:+UseDynamicNumberOfGCThreads", jvmOptions.getUseDynamicNumberOfGcThreads(),
                "UseDynamicNumberOfGCThreads not correct.");
    }

    @Test
    void testVerify() {
        String jvmArgs = "-Xverify:none";
        JvmOptions jvmOptions = new JvmOptions(jvmArgs);
        assertEquals("-Xverify:none", jvmOptions.getVerify(), "Verify not correct.");
    }
}
