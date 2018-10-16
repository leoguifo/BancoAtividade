package banco.db;

import banco.modelo.Conta;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContaDB {
    
private static final String URL = "jdbc:sqlite:banco.db";
    private static final String TABLE = "conta";
    private Connection conn;

    private PreparedStatement selectTodos;
    private PreparedStatement insertNovo;
    private PreparedStatement update;
    private PreparedStatement delete;
    private PreparedStatement deleteAll;
    
    public ContaDB() {
    try {
            conn = DriverManager.getConnection(URL);

            createTable();

            selectTodos = conn.prepareStatement("SELECT * FROM conta");
            insertNovo = conn.prepareStatement("INSERT INTO conta (agencia, numero, saldo, idCliente) VALUES (?,?,?,?)");
            update = conn.prepareStatement("UPDATE conta SET agencia=?, numero=?, saldo=?, idCliente=? WHERE id=?");
            delete = conn.prepareStatement("DELETE FROM conta WHERE id=?");
            deleteAll = conn.prepareStatement("DELETE FROM conta");

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void createTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TABLE
                + "  (id                INTEGER,"
                + "   agencia           INTEGER,"
                + "   numero            INTEGER,"
                + "   saldo             DOUBLE,"
                + "   idCliente         INTEGER,"
                + "   FOREIGN KEY (idCliente)  REFERENCES cliente(id)"
                + "   PRIMARY KEY (id))";
        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate);
    }

    private Conta getContaFromRs(ResultSet rs) throws SQLException {
        return new Conta(
                rs.getInt("id"),
                rs.getInt("agencia"),
                rs.getInt("numero"),
                rs.getDouble("saldo"),
                new ClienteDB().getPessoa(rs.getInt("idCliente"))
        );
    }

    // obtém todas as pessoas
    public List<Conta> getContas() {
        List<Conta> resultado = null;
        ResultSet rs = null;

        try {
            rs = selectTodos.executeQuery();
            resultado = new ArrayList<>();

            while (rs.next()) {
                resultado.add(getContaFromRs(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                close();
            }
        }
        return resultado;
    }

    public int addConta(Conta c) {
        int resultado = addConta(c.getAgencia(), c.getNumero(), c.getSaldo(), c.getCliente().getId());

        try {

            ResultSet generatedKeys = insertNovo.getGeneratedKeys();

            if (generatedKeys.next()) {
                c.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating client failed, no ID obtained.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return resultado;
    }

    // adiciona uma pessoa
    public int addConta(int agencia, int numero, double saldo, int idCliente) {
        int resultado = 0;

        try {
            insertNovo.setInt(1, agencia);
            insertNovo.setInt(2, numero);
            insertNovo.setDouble(3, saldo);
            insertNovo.setInt(4, idCliente);

            // insere e retorna o numero de linhas atualizadas
            resultado = insertNovo.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return resultado;
    }

    public int updateConta(Conta c) {
        return updateConta(c.getId(), c.getAgencia(), c.getNumero(), c.getSaldo(), c.getCliente().getId());
    }

    public int updateConta(int id, int agencia, int numero, double saldo, int idCliente) {
        int resultado = 0;

        try {
            update.setInt(1, agencia);
            update.setInt(2, numero);
            update.setDouble(3, saldo);
            update.setInt(4, idCliente);

            update.setInt(5, id);

            // retorna o numero de linhas atualizadas
            resultado = update.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return resultado;
    }

    public int deleteConta(Conta c) {
        return deleteConta(c.getId());
    }

    public int deleteConta(int id) {
        int resultado = 0;

        try {
            delete.setInt(1, id);
            // deleta e retorna o numero de linhas atualizadas
            resultado = delete.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return resultado;
    }

    public int deleteAllEntries() {
        int resultado = 0;

        try {
            // deleta e retorna o numero de linhas atualizadas
            resultado = deleteAll.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return resultado;
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
