// Função para exibir a senha (mostrar/esconder)
    function togglePassword() {
      var senha = document.getElementById("senha");
      if (senha.type === "password") {
        senha.type = "text"; // Mudar para mostrar a senha
      } else {
        senha.type = "password"; // Mudar para esconder a senha
      }
    }
    document.getElementById("botaosenha").addEventListener("click", function() {
      this.classList.toggle("ativo");
    });

    // Função para enviar os dados e atualizar a query string
    // Função para enviar os dados e atualizar a query string


    // Função para atualizar os dados da página com base na query string
    function atualizarExibicao() {
      const params = new URLSearchParams(window.location.search);

      // Pega os parâmetros da query string
      const tipo = params.get('tipo') || 'Não informado';
      const usuario = params.get('usuario') || 'Não informado';
      const senha = params.get('senha') || 'Não informada';

      // Atualiza os elementos HTML com os valores
      document.getElementById('usuarioDisplay').textContent = usuario;
      document.getElementById('senhaDisplay').textContent = senha;
      document.getElementById('tipoDisplay').textContent = tipo;

      // Atualiza os campos de input com os valores da query string (facilita o preenchimento após a atualização)
      document.getElementById('usuario').value = usuario;
      document.getElementById('senha').value = senha;
      document.getElementById('tipo').value = tipo;
    }

    // Função para limpar os campos de entrada e as exibições

    // function limparCampos() {
    //   // Limpar campos de input
    //   document.getElementById('usuario').value = '';
    //   document.getElementById('senha').value = '';
    //   document.getElementById('tipo').value = 'aluno'; // Definido como padrão, pode ser 'professor' também
    //   // Limpar exibições de dados
    // }

    // Atualiza os dados assim que a página é carregada
    window.onload = atualizarExibicao;

    function enviarDados() {
        const usuario = document.getElementById('usuario').value.trim();
        const senha = document.getElementById('senha').value;
        const tipo = document.getElementById('tipo').value;

        if (!usuario || !senha) {
          alert('Por favor, preencha o nome e a senha.');
          return;
        }
        if(tipo==="Selecionar"){
          alert('Adicione um perfil de usuario.')
          return
        }

        const url = new URL(window.location.href);

        url.searchParams.set('tipo', tipo);
        url.searchParams.set('usuario', usuario);
        url.searchParams.set('senha', senha);

        window.history.pushState({}, '', url);

        atualizarExibicao();
        limparCampos();
        window.location.reload();
    }