package org.unipd.nbeghin.weka;

/**
 * Created by Nicola Beghin on 12/09/13.
 */
public class WekaClassifier {

    private static String[] classifications=new String[]{"NONSTAIR", "STAIR"};

    public static String explicit_classify(Object[] i) throws Exception {
        return classifications[(int)classify(i)];
    }

    public static double classify(Object[] i) throws Exception {
        double p = Double.NaN;
        p = WekaClassifier.N4f3ad88b0(i);
        return p;
    }
    static double N4f3ad88b0(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 0;
        } else if (((Double) i[1]).doubleValue() <= 0.06) {
            p = 0;
        } else if (((Double) i[1]).doubleValue() > 0.06) {
            p = WekaClassifier.N205eb6501(i);
        }
        return p;
    }
    static double N205eb6501(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() <= 0.75) {
            p = WekaClassifier.N2a134eca2(i);
        } else if (((Double) i[3]).doubleValue() > 0.75) {
            p = WekaClassifier.N71bd899313(i);
        }
        return p;
    }
    static double N2a134eca2(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 1;
        } else if (((Double) i[10]).doubleValue() <= 0.09) {
            p = WekaClassifier.N5999c55a3(i);
        } else if (((Double) i[10]).doubleValue() > 0.09) {
            p = WekaClassifier.N7bc5b82812(i);
        }
        return p;
    }
    static double N5999c55a3(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 0.48) {
            p = WekaClassifier.N7e6bc5aa4(i);
        } else if (((Double) i[6]).doubleValue() > 0.48) {
            p = 1;
        }
        return p;
    }
    static double N7e6bc5aa4(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 1;
        } else if (((Double) i[11]).doubleValue() <= 0.0) {
            p = WekaClassifier.N557e5cbd5(i);
        } else if (((Double) i[11]).doubleValue() > 0.0) {
            p = WekaClassifier.N50d79dfe11(i);
        }
        return p;
    }
    static double N557e5cbd5(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 0.2) {
            p = WekaClassifier.N5afaa8246(i);
        } else if (((Double) i[6]).doubleValue() > 0.2) {
            p = WekaClassifier.N602f892f10(i);
        }
        return p;
    }
    static double N5afaa8246(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 0.01) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() > 0.01) {
            p = WekaClassifier.N7e3502257(i);
        }
        return p;
    }
    static double N7e3502257(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() <= 0.02) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() > 0.02) {
            p = WekaClassifier.N4a200bde8(i);
        }
        return p;
    }
    static double N4a200bde8(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() <= 0.05) {
            p = WekaClassifier.N7deb41d69(i);
        } else if (((Double) i[7]).doubleValue() > 0.05) {
            p = 1;
        }
        return p;
    }
    static double N7deb41d69(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 0;
        } else if (((Double) i[1]).doubleValue() <= 0.14) {
            p = 0;
        } else if (((Double) i[1]).doubleValue() > 0.14) {
            p = 1;
        }
        return p;
    }
    static double N602f892f10(Object []i) {
        double p = Double.NaN;
        if (i[8] == null) {
            p = 1;
        } else if (((Double) i[8]).doubleValue() <= 0.01) {
            p = 1;
        } else if (((Double) i[8]).doubleValue() > 0.01) {
            p = 0;
        }
        return p;
    }
    static double N50d79dfe11(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 0.41) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() > 0.41) {
            p = 0;
        }
        return p;
    }
    static double N7bc5b82812(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 0.31) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() > 0.31) {
            p = 0;
        }
        return p;
    }
    static double N71bd899313(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 0.01) {
            p = WekaClassifier.N52934c3b14(i);
        } else if (((Double) i[2]).doubleValue() > 0.01) {
            p = WekaClassifier.N5d53d05b18(i);
        }
        return p;
    }
    static double N52934c3b14(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 0.11) {
            p = WekaClassifier.N4471dfd715(i);
        } else if (((Double) i[6]).doubleValue() > 0.11) {
            p = WekaClassifier.N101ebf5c16(i);
        }
        return p;
    }
    static double N4471dfd715(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 0;
        } else if (((Double) i[10]).doubleValue() <= 0.03) {
            p = 0;
        } else if (((Double) i[10]).doubleValue() > 0.03) {
            p = 1;
        }
        return p;
    }
    static double N101ebf5c16(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 0;
        } else if (((Double) i[6]).doubleValue() <= 0.25) {
            p = 0;
        } else if (((Double) i[6]).doubleValue() > 0.25) {
            p = WekaClassifier.N52a5394817(i);
        }
        return p;
    }
    static double N52a5394817(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 1;
        } else if (((Double) i[11]).doubleValue() <= 0.0) {
            p = 1;
        } else if (((Double) i[11]).doubleValue() > 0.0) {
            p = 0;
        }
        return p;
    }
    static double N5d53d05b18(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 0.1) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() > 0.1) {
            p = WekaClassifier.N2994363b19(i);
        }
        return p;
    }
    static double N2994363b19(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 0.14) {
            p = WekaClassifier.N417470d020(i);
        } else if (((Double) i[1]).doubleValue() > 0.14) {
            p = WekaClassifier.N747541f825(i);
        }
        return p;
    }
    static double N417470d020(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() <= 0.02) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() > 0.02) {
            p = WekaClassifier.N439a894221(i);
        }
        return p;
    }
    static double N439a894221(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() <= 0.07) {
            p = WekaClassifier.N56a96eba22(i);
        } else if (((Double) i[9]).doubleValue() > 0.07) {
            p = WekaClassifier.N33799a1e24(i);
        }
        return p;
    }
    static double N56a96eba22(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 0.15) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() > 0.15) {
            p = WekaClassifier.Nda4a1c923(i);
        }
        return p;
    }
    static double Nda4a1c923(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 0.79) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 0.79) {
            p = 1;
        }
        return p;
    }
    static double N33799a1e24(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 0;
        } else if (((Double) i[6]).doubleValue() <= 0.21) {
            p = 0;
        } else if (((Double) i[6]).doubleValue() > 0.21) {
            p = 1;
        }
        return p;
    }
    static double N747541f825(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 0.17) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() > 0.17) {
            p = 0;
        }
        return p;
    }
}
