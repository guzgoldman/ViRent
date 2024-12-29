package project.db;
import java.util.List;

public interface DAO<T> {
	void insertar(T obj);
	void actualizar(T obj);
	T buscarPorId(int id);
	T buscarPorIdentificador(String id);
	List<T> listarTodos();
}
