package optimizer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class OptimizerManager {
    private ArrayList<String> lines;
    private ArrayList<String> data;
    private HashMap<String, String> map;

    public OptimizerManager() {
        this.lines = new ArrayList<>();
        this.data = new ArrayList<>();
        map = new HashMap<>();
    }

    public void optimize(File file) throws IOException {
        getTACLines(file);
        //optimizeRedundantAssigns();
        refactorVarsNames();
        writeTACtoFile(file);
    }

    // @TODO finish swap and remove
    private void optimizeRedundantAssigns() {
        for (int i = 0; i < lines.size(); i++) {
            // Search for case t0 := t1
            String[] parts = lines.get(i).split(" ");
            if (parts.length == 3 && parts[0].matches("^(\\d*[a-zA-Z_]+\\w*)$") &&
                    parts[2].matches("^(\\d*[a-zA-Z_]+\\w*)$") && !parts[0].equals("true") && !parts[0].equals("false")
                        && !parts[2].equals("true") && !parts[2].equals("false")) {
                // look for t1 and substitute all
                swapAndRemove(parts[2], parts[0], i);
            }
        }
    }

    // @TODO finish swap and remove
    private void swapAndRemove(String b, String a, int start) {
        for (int i = start; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(" ");

            if (parts.length == 3) {
                // case t0 := t1
                if (parts[0].equals(a)) {
                    parts[0] = b;
                }

                if (parts[2].equals(a)) {
                    parts[2] = b;
                }

                if (parts[0].equals(parts[2])) {
                    // Case remove
                    // lines.remove(i);
                    // i--;
                } else {
                    lines.set(i, parts[0] + " " + parts[1] + " " + parts[2]);
                }
            } else if (parts.length == 5) {
                if (lines.get(i).contains("goto")) {
                    if (lines.get(i).contains("!")) {
                        // L0: if !t3 goto L1
                        if (parts[2].equals("!" + a)) {
                            parts[2] = "!" + b;
                        }
                        lines.set(i, parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4]);
                    } else {
                        // L0: if t3 goto L1
                        if (parts[2].equals(a)) {
                            parts[2] = b;
                        }
                        lines.set(i, parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4]);
                    }
                } else {
                    // case t0 := t1 + t2
                    if (parts[0].equals(a)) {
                        parts[0] = b;
                    }

                    if (parts[2].equals(a)) {
                        parts[2] = b;
                    }

                    if (parts[4].equals(a)) {
                        parts[4] = b;
                    }
                    lines.set(i, parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4]);
                }
            } else if (parts.length == 2) {
                // case goto L0
            } else if (parts.length == 1) {
                // case L0:
            } else if (parts.length == 7) {
                if (parts[2].equals(a)) {
                    parts[2] = b;
                }

                if (parts[4].equals(a)) {
                    parts[4] = b;
                }

                data.add(parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4] + " " + parts[5] + " " + parts[6]);
            } else {
                System.out.println("ERROR_B");
            }
        }
    }

    private void refactorVarsNames() {
        int index = 0;
        for (int i = 0; i < lines.size(); i++) {
            String parts[] = lines.get(i).split(" ");

            if (parts.length == 3) {
                // case t0 := t1
                String t0;
                if (parts[0].matches("^(\\d*[a-zA-Z_]+\\w*)$") && !parts[0].equals("true") && !parts[0].equals("false")) {
                    // is variable
                    if (map.containsKey(parts[0])) {
                        t0 = map.get(parts[0]);
                    } else {
                        t0 = "t" + index;
                        map.put(parts[0], t0);
                        index++;
                    }
                } else {
                    // is number
                    t0 = parts[0];
                }

                String t1;
                if (parts[2].matches("^(\\d*[a-zA-Z_]+\\w*)$") && !parts[2].equals("true") && !parts[2].equals("false")) {
                    // is variable
                    if (map.containsKey(parts[2])) {
                        t1 = map.get(parts[2]);
                    } else {
                        t1 = "t" + index;
                        map.put(parts[2], t1);
                        index++;
                    }
                } else {
                    // is number
                    t1 = parts[2];
                }

                data.add(t0 + " " + parts[1] + " " + t1);
            } else if (parts.length == 5) {
                if (lines.get(i).contains("goto")) {
                    if (lines.get(i).contains("!")) {
                        // L0: if !t3 goto L1
                        String t0;
                        if (parts[2].matches("^(\\d*[a-zA-Z_]+\\w*)$") && !parts[2].equals("true") && !parts[2].equals("false")) {
                            // is variable
                            if (map.containsKey(parts[2].replace("!", ""))) {
                                t0 = map.get(parts[2].replace("!", ""));
                            } else {
                                t0 = "t" + index;
                                map.put(parts[2].replace("!", ""), t0);
                                index++;
                            }
                        } else {
                            // is number or bool
                            t0 = parts[2].replace("!", "");
                        }
                        data.add(parts[0] + " " + parts[1] + " !" + t0 + " " + parts[3] + " " + parts[4]);
                    } else {
                        // L0: if t3 goto L1
                        String t0;
                        if (parts[2].matches("^(\\d*[a-zA-Z_]+\\w*)$") && !parts[2].equals("true") && !parts[2].equals("false")) {
                            // is variable
                            if (map.containsKey(parts[2])) {
                                t0 = map.get(parts[2]);
                            } else {
                                t0 = "t" + index;
                                map.put(parts[2], t0);
                                index++;
                            }
                        } else {
                            // is number or bool
                            t0 = parts[2];
                        }
                        data.add(parts[0] + " " + parts[1] + " " + t0 + " " + parts[3] + " " + parts[4]);
                    }
                } else {
                    // case t0 := t1 + t2
                    String t0;
                    if (parts[0].matches("^(\\d*[a-zA-Z_]+\\w*)$") && !parts[0].equals("true") && !parts[0].equals("false")) {
                        // is variable
                        if (map.containsKey(parts[0])) {
                            t0 = map.get(parts[0]);
                        } else {
                            t0 = "t" + index;
                            map.put(parts[0], t0);
                            index++;
                        }
                    } else {
                        // is number or bool
                        t0 = parts[0];
                    }

                    String t1;
                    if (parts[2].matches("^(\\d*[a-zA-Z_]+\\w*)$") && !parts[2].equals("true") && !parts[2].equals("false")) {
                        // is variable
                        if (map.containsKey(parts[2])) {
                            t1 = map.get(parts[2]);
                        } else {
                            t1 = "t" + index;
                            map.put(parts[2], t1);
                            index++;
                        }
                    } else {
                        // is number or bool
                        t1 = parts[2];
                    }

                    String t2;
                    if (parts[4].matches("^(\\d*[a-zA-Z_]+\\w*)$") && !parts[4].equals("true") && !parts[4].equals("false")) {
                        // is variable
                        if (map.containsKey(parts[4])) {
                            t2 = map.get(parts[4]);
                        } else {
                            t2 = "t" + index;
                            map.put(parts[4], t2);
                            index++;
                        }
                    } else {
                        // is number or bool
                        t2 = parts[4];
                    }

                    data.add(t0 + " " + parts[1] + " " + t1 + " " + parts[3] + " " + t2);
                }
            } else if (parts.length == 2) {
                if (lines.get(i).contains("goto")) {
                    // case goto L0
                    data.add(lines.get(i));
                } else if (lines.get(i).contains("PopParam") || lines.get(i).contains("PushParam")) {
                    // case PopParam t0 OR PushParam t1
                    String t0;
                    if (map.containsKey(parts[1])) {
                        t0 = map.get(parts[1]);
                    } else {
                        if (parts[1].matches("^(\\d*[a-zA-Z_]+\\w*)$") && !parts[1].equals("true") && !parts[1].equals("false")) {
                            // is variable
                            if (map.containsKey(parts[1])) {
                                t0 = map.get(parts[1]);
                            } else {
                                t0 = "t" + index;
                                map.put(parts[1], t0);
                                index++;
                            }
                        } else {
                            // is number
                            t0 = parts[1];
                        }
                    }
                    data.add(parts[0] + " " + t0);
                } else {
                    data.add(lines.get(i));
                }
            } else if (parts.length == 1) {
                // case L0:
                data.add(lines.get(i));
            } else if (parts.length == 7) {
                // case L0: if t0 >= t1 goto L2
                String t0;
                if (parts[2].matches("^(\\d*[a-zA-Z_]+\\w*)$") && !parts[2].equals("true") && !parts[2].equals("false")) {
                    // is variable
                    if (map.containsKey(parts[2])) {
                        t0 = map.get(parts[2]);
                    } else {
                        t0 = "t" + index;
                        map.put(parts[2], t0);
                        index++;
                    }
                } else {
                    // is number
                    t0 = parts[2];
                }

                String t1;
                if (parts[4].matches("^(\\d*[a-zA-Z_]+\\w*)$") && !parts[4].equals("true") && !parts[4].equals("false")) {
                    // is variable
                    if (map.containsKey(parts[4])) {
                        t1 = map.get(parts[4]);
                    } else {
                        t1 = "t" + index;
                        map.put(parts[4], t1);
                        index++;
                    }
                } else {
                    // is number
                    t1 = parts[4];
                }

                data.add(parts[0] + " " + parts[1] + " " + t0 + " " + parts[3] + " " + t1 + " " + parts[5] + " " + parts[6]);
            } else {
                data.add("ERROR_A");
            }
        }
    }

    private void getTACLines(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        for(String line; (line = br.readLine()) != null; ) {
            lines.add(line);
        }
        br.close();
    }

    private void writeTACtoFile(File file) throws IOException {
        FileWriter myWriter = new FileWriter(file);
        for (int i = 0; i < data.size(); i++) {
            myWriter.write(data.get(i));
            myWriter.write("\n");
        }
        myWriter.close();
    }
}
