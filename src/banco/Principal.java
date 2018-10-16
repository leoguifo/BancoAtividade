package banco;

import banco.db.ClienteDB;
import banco.db.ContaDB;
import banco.modelo.Cliente;
import banco.modelo.Conta;

public class Principal {

    public static void main(String[] args) {
        ClienteDB clienteDB = new ClienteDB();
        ContaDB contaDB = new ContaDB();
        
        clienteDB.deleteAllEntries();
        contaDB.deleteAllEntries();

        Cliente pessoa = new Cliente(0, "João", "Rua Temática", 2323022, 6606606, 32789012, 4500);
        clienteDB.addCliente(pessoa);
        
        Cliente pessoa1 = new Cliente(0, "Juca", "Rua Temática", 2323022, 6606606, 32789012, 4500);
        clienteDB.addCliente(pessoa1);

        Conta conta1 = new Conta(0, 2323, 1, 5000, pessoa);
        contaDB.addConta(conta1);
        
        Conta conta2 = new Conta(1, 4444, 2, 250, pessoa1);
        contaDB.addConta(conta2);

        for (Conta conta : contaDB.getContas()) {
            System.out.println("Id: " + conta.getId());
            System.out.println("Agencia: " + conta.getAgencia() + "     " + "Numero Conta: " + conta.getNumero() + "      " + "Saldo:" + conta.getSaldo());
            System.out.println("Nome: " + conta.getCliente().getNome());
            System.out.println("Endereco: " + conta.getCliente().getEndereco());
            System.out.println("CPF: " + conta.getCliente().getCpf());
            System.out.println("RG: " + conta.getCliente().getRg());
            System.out.println("Telefone: " + conta.getCliente().getTelefone());
            System.out.println("Renda Mensal: " + conta.getCliente().getRendaMensal());
        }
    }
}
