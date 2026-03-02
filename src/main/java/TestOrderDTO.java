public class TestOrderDTO {

    private int testOrderID;
    private String fullName;
    private String testName;
    private String status;

    public TestOrderDTO() {
    }

    public TestOrderDTO(int testOrderID, String fullName, String testName, String status) {
        this.testOrderID = testOrderID;
        this.fullName = fullName;
        this.testName = testName;
        this.status = status;
    }

    public int getTestOrderID() {
        return testOrderID;
    }

    public void setTestOrderID(int testOrderID) {
        this.testOrderID = testOrderID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}