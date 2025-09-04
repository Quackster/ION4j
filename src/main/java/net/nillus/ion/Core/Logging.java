package net.nillus.ion.Core;

public class Logging {
    // Fields
    private LogType mMinimumLogImportancy;
    private boolean mBusy;

    // Properties
    public LogType getMinimumLogImportancy() {
        return mMinimumLogImportancy;
    }

    public void setMinimumLogImportancy(LogType value) {
        if (!value.equals(mMinimumLogImportancy)) {
            // TODO: log change
        }
        mMinimumLogImportancy = value;
    }

    // Methods
    private void writeLineInternal(String sLine, LogType pLogType, boolean ignoreLogType) {
        while (mBusy) { } // Hang on /b/rotahs
        mBusy = true;

        System.out.print("[" + new java.util.Date().toString() + "]");
        System.out.print(" -- ");

        if (pLogType == LogType.Information)
            System.out.print("\u001B[36m"); // Dark Cyan
        else if (pLogType == LogType.Warning)
            System.out.print("\u001B[33m"); // Dark Yellow
        else if (pLogType == LogType.Error)
            System.out.print("\u001B[31m"); // Red
        else if (pLogType == LogType.Debug)
            System.out.print("\u001B[90m"); // Dark Gray
        else
            System.out.print("\u001B[37m"); // Gray

        System.out.println(sLine);
        System.out.print("\u001B[37m"); // Reset to default color

        mBusy = false;
    }

    public void writeLine(String sLine) {
        writeLineInternal(sLine, LogType.Information, true);
    }

    public void writeInformation(String sLine) {
        if (mMinimumLogImportancy.ordinal() <= LogType.Information.ordinal())
            writeLineInternal(sLine, LogType.Information, false);
    }

    public void writeWarning(String sLine) {
        if (mMinimumLogImportancy.ordinal() <= LogType.Warning.ordinal())
            writeLineInternal(sLine, LogType.Warning, false);
    }

    public void writeError(String sLine) {
        if (mMinimumLogImportancy.ordinal() <= LogType.Error.ordinal())
            writeLineInternal(sLine, LogType.Error, false);
    }

    public void writeUnhandledExceptionError(String sMethodName, Throwable ex) {
        writeError("Unhandled exception in " + sMethodName + "() method, "
                + "exception message: " + ex.getMessage() + ", "
                + "stack trace: " + ex.getStackTrace());
    }

    public void writeConfigurationParseError(String sField) {
        if (mMinimumLogImportancy.ordinal() <= LogType.Error.ordinal()) {
            String sLine = String.format("Could not parse configuration field '%s'.", sField);
            writeLineInternal(sLine, LogType.Error, false);
        }
    }
}

