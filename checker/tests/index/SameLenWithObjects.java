import org.checkerframework.checker.index.qual.*;

class SameLenWithObjects {

    class SimpleCollection {
        Object[] var_infos;
    }

    static final class Invocation0 {
        SimpleCollection sc;
        Object @SameLen({"vals1", "sc.var_infos"}) [] vals1;

        void format1() {
            for (int j = 0; j < vals1.length; j++) {
                System.out.println(sc.var_infos[j]);
            }
        }
    }

    static final class PptTopLevel {
        final String[] var_infos;

        PptTopLevel(String[] var_infos) {
            this.var_infos = var_infos;
        }
    }

    static final class Invocation1 {
        PptTopLevel ppt;
        Object @SameLen({"vals1", "ppt.var_infos"}) [] vals1;

        void format1() {
            for (int j = 0; j < vals1.length; j++) {
                System.out.println(ppt.var_infos[j]);
            }
        }
    }

    static final class Invocation2 {
        Object @SameLen({"vals2", "mods2"}) [] vals2;
        int @SameLen({"vals2", "mods2"}) [] mods2;

        void format2() {
            for (int j = 0; j < vals2.length; j++) {
                System.out.println(mods2[j]);
            }
        }
    }
}
