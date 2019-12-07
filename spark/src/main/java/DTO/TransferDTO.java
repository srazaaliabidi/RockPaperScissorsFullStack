package DTO;

public class TransferDTO {
    public String name;
    public String choice;

    public TransferDTO(String name, String choice) {
        this.name = name;
        this.choice = choice;
    }

    public String toString() {
        return "\n" + this.name + "\n" + this.choice + "\n";
    }
}
