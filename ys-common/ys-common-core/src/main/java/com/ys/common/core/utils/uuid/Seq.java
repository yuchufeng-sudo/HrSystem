package com.ys.common.core.utils.uuid;

import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ys
 */
public class Seq
{

    public static final String commSeqType = "COMMON";


    public static final String uploadSeqType = "UPLOAD";


    private static AtomicInteger commSeq = new AtomicInteger(1);

    private static AtomicInteger uploadSeq = new AtomicInteger(1);


    private static final String machineCode = "A";

    /**
     *
     *
     * @return
     */
    public static String getId()
    {
        return getId(commSeqType);
    }

    /**
     *
     *
     * @return
     */
    public static String getId(String type)
    {
        AtomicInteger atomicInt = commSeq;
        if (uploadSeqType.equals(type))
        {
            atomicInt = uploadSeq;
        }
        return getId(atomicInt, 3);
    }

    /**
     *
     *
     * @param atomicInt
     * @param length
     * @return
     */
    public static String getId(AtomicInteger atomicInt, int length)
    {
        String result = DateUtils.dateTimeNow();
        result += machineCode;
        result += getSeq(atomicInt, length);
        return result;
    }

    /**
     *
     *
     * @return
     */
    private synchronized static String getSeq(AtomicInteger atomicInt, int length)
    {

        int value = atomicInt.getAndIncrement();


        int maxSeq = (int) Math.pow(10, length);
        if (atomicInt.get() >= maxSeq)
        {
            atomicInt.set(1);
        }

        return StringUtils.padl(value, length);
    }
}
