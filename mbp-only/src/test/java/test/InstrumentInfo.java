package test;

public class InstrumentInfo {
    final int instrumentId;
    final String desc;

    public InstrumentInfo(final int instrumentId, final String desc) {
        this.instrumentId = instrumentId;
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstrumentInfo that = (InstrumentInfo) o;

        if (instrumentId != that.instrumentId) return false;
        return desc.equals(that.desc);

    }

    @Override
    public int hashCode() {
        int result = instrumentId;
        result = 31 * result + desc.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Instrument{" + instrumentId + ", '" + desc + "'}";
    }
}
