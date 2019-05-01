

//Inicialização do canvas
var canvas = new fabric.Canvas(document.getElementById('canvas'));

//Impedir que objetos do canvas sejam selecionados em grupo
canvas.selection = false;

//Event listener para o clique do mouse
//Clicando em uma área vazia um novo quadrado é criado
//Clicando em um quadrado o menu de propriedades é ativado
canvas.on('mouse:down', function(event)
		{
	if(!event.target)
	{

		conteudoPropriedade();

		var pointer = canvas.getPointer(event.e);
		var rect = new fabric.Rect(
				{
					fill: 'red',
					opacity: 0.3,
					top : pointer.y,
					left : pointer.x,
					width : 40,
					height : 40,
					fill : 'red',
					originX: 'center',
					originY: 'center',
					sala: ""
				});

		canvas.add(rect);
	}
	else 
	{ 
		conteudoPropriedade();
	}


		});

//Listener eventos de deixar de clicar no mouse
//Em branco para evitar eventos padrão da biblioteca
canvas.on('mouse:up', function(e)
		{
		});

//Lê o arquivo inserido pelo usuário e aplica no background do canvas
//width e height são ajustados para o mesmo tamanho do canvas
document.getElementById('arquivo').addEventListener("change", function(e) {
	  var file = e.target.files[0];
	  var reader = new FileReader();
	  console.log("reader   " + reader);
	  reader.onload = function(f) {
	    var data = f.target.result;
	    fabric.Image.fromURL(data, function(img) {
	    	
	    	altura= canvas.getHeight();
			largura = canvas.getWidth();
	    	
	      var oImg = img.set({
	    	  height: altura , width: largura
	      });
	      canvas.setBackgroundImage(oImg).renderAll();

	    });
	  };
	  reader.readAsDataURL(file);
	});


//Controla o conteudo do painel lateral
function conteudoPropriedade() 
{ 
	if(canvas.getActiveObject())
	{
		propriedadeTexto = document.getElementById('propriedade');

		objeto = canvas.getActiveObject();
		texto = objeto.toObject().sala;

		propriedadeTexto.innerHTML =  '<div class="form-group">' +
		'<label for="idsala">Número da Sala</label>' +
		'<input type="text" class="form-control" id="idSala"  value="' +  texto + '" >'  +
		'</div>' +
		'<button type="button" onclick="alterarPropriedade();" class="btn btn-default">Aplicar</button>';

	} 
	else 
	{ 
		propriedadeTexto = document.getElementById('propriedade');
		propriedadeTexto.innerHTML = "<p>Nenhum item selecionado</p>";
	}
}

//Ao ser chamada essa função altera o valor da propriedade "sala"
function alterarPropriedade() 
{ 
	valor = document.getElementById('idSala').value;
	objeto = canvas.getActiveObject();
	objeto.sala= valor;
	objeto.setColor("green");
	canvas.renderAll();

	console.log("Modificado", objeto.sala);

}

//Apaga o objeto selecionado
function apagar() 
{ 
	if(canvas.getActiveObject()) 
	{
		canvas.getActiveObject().remove();
		conteudoPropriedade();
	}

}

//Apaga todos os objetos do tipo retângulo - O backgroun permanesse intacto
function limpar() 
{

	listaObjetos = canvas.getObjects('rect');
	for(var i = 0; i < listaObjetos.length; i++) 
	{
		listaObjetos[i].remove();
	}
	conteudoPropriedade();
}


function salvar() 
{
	canvasJson = canvas.toJSON();
	delete canvasJson.backgroundImage;
	document.getElementById('mapeamento').value = JSON.stringify(canvasJson);
	
}


$(document).ready(function(){
    $('#salvarTudo').click(function(e){

    	listaObjetos = canvas.getObjects('rect');
   
    	if(listaObjetos.length == 0) {
    		console.log("Não salvei");

    		e.preventDefault();

			$('#erroMsg').val("Não é possível salvar uma planta sem salas mapeadas");
    		$('#erroSalvar').modal('show');
	    		
    	} else {
    		
    		if(checarSalaSemNumero(listaObjetos)){
    			salvar();
            	e.submit();
    			
    		} else { 
    			
   		     	 e.preventDefault();  			  			

    			$('#erroMsg').text("Existem salas sem numeração");
    			$('#erroSalvar').modal('show');
    		}
       }
    	
    });
});


function checarSalaSemNumero(salas){ 
	console.log(salas.length);
	 var object = null;
	 for (var i = 0, len = salas.length; i < len; i++) {
		 if(!salas[i].sala) {
		     canvas.setActiveObject(salas[i]);
		     salas[i].setColor('yellow');
		     conteudoPropriedade();
		 	canvas.renderAll();
			 return false;
		 }
  }
	 return true;
}





//Sobrecarrega a classe Object do fabric.js para inclui nativamente a propriedade "sala"
fabric.Object.prototype.toObject = (function (toObject) {
	return function () {
		return fabric.util.object.extend(toObject.call(this), {
			sala: this.sala,
			idSala : this.idSala
		});
	};
})(fabric.Object.prototype.toObject);