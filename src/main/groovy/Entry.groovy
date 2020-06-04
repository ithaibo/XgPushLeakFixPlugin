import com.andy.plugin.scan.AsmEntry

class Entry {
    static void main(String[] args) {
        AsmEntry.scanClassFile(new File("g\$4.class"))
        AsmEntry.scanClassFile(new File("g\$5.class"))
        AsmEntry.scanClassFile(new File("d.class"))
    }
}
