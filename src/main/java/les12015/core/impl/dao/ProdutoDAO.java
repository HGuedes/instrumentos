
package les12015.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import les12015.dominio.EntidadeDominio;
import les12015.dominio.Produto;

public class ProdutoDAO extends AbstractJdbcDAO {
	
	public ProdutoDAO() {
		super("tb_produto", "id_pro");		
	}
	public void salvar(EntidadeDominio entidade) {
		openConnection();
		PreparedStatement pst=null;
		Produto produto = (Produto)entidade;
		
		
		try {
			connection.setAutoCommit(false);			
					
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO tb_produto(descricao, quantidade, ");
			sql.append("dt_cadastro) VALUES (?,?,?)");		
			
			pst = connection.prepareStatement(sql.toString(), 
					Statement.RETURN_GENERATED_KEYS);
			
			pst.setString(1, produto.getDescricao());
			pst.setInt(2, produto.getQuantidade());
			Timestamp time = new Timestamp(produto.getDtCadastro().getTime());
			pst.setTimestamp(3, time);
			pst.executeUpdate();	
			
			ResultSet rs = pst.getGeneratedKeys();
			int id=0;
			if(rs.next())
				id = rs.getInt(1);
			produto.setId(id);
			
			connection.commit();		
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();			
		}finally{
			try {
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		

	}
	/** 
	 * TODO Descri��o do M�todo
	 * @param entidade
	 * @see fai.dao.IDAO#alterar(fai.domain.EntidadeDominio)
	 */
	public void alterar(EntidadeDominio entidade) {
		openConnection();
		PreparedStatement pst=null;
		Produto produto = (Produto)entidade;		
		
		try {
			connection.setAutoCommit(false);			
					
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE tb_produto SET descricao=?, quantidade=? ");
			sql.append("WHERE id_pro=?");				
			
					
			pst = connection.prepareStatement(sql.toString());
			pst.setString(1, produto.getDescricao());
			pst.setInt(2, produto.getQuantidade());
			pst.setInt(3, produto.getId());
			pst.executeUpdate();			
			connection.commit();		
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();			
		}finally{
			try {
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		
	}
	/** 
	 * TODO Descri��o do M�todo
	 * @param entidade
	 * @return
	 * @see fai.dao.IDAO#consulta(fai.domain.EntidadeDominio)
	 */
	public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
			PreparedStatement pst = null;
			
			Produto produto = (Produto)entidade;
			String sql=null;
			
			if(produto.getDescricao() == null){
				produto.setDescricao("");
			}
			
			if(produto.getId() == null && produto.getDescricao().equals("")){
				sql = "SELECT * FROM tb_produto";
			}else if(produto.getId() != null && produto.getDescricao().equals("")){
				sql = "SELECT * FROM tb_produto WHERE id_pro=?";
			}else if(produto.getId() == null && !produto.getDescricao().equals("")){
				sql = "SELECT * FROM tb_produto WHERE descricao like ?";
			
			}
		
		
		
		try {
			openConnection();
			pst = connection.prepareStatement(sql);
			
			if(produto.getId() != null && produto.getDescricao().equals("")){
				pst.setInt(1, produto.getId());
			}else if(produto.getId() == null && !produto.getDescricao().equals("")){
				pst.setString(1, "%"+produto.getDescricao()+"%");			
			}
			

			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> produtos = new ArrayList<EntidadeDominio>();
			while (rs.next()) {
				Produto p = new Produto();
				p.setId(rs.getInt("id_pro"));
				p.setDescricao(rs.getString("descricao"));
				p.setQuantidade(rs.getInt("quantidade"));
				
				java.sql.Date dtCadastroEmLong = rs.getDate("dt_cadastro");
				Date dtCadastro = new Date(dtCadastroEmLong.getTime());				
				p.setDtCadastro(dtCadastro);
				produtos.add(p);
			}
			return produtos;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	

	

}
