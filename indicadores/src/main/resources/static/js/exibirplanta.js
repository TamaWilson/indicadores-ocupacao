/**
 * 
 */






//Sobrecarrega a classe Object do fabric.js para inclui nativamente a propriedade "sala"
fabric.Object.prototype.toObject = (function (toObject) {
	return function () {
		return fabric.util.object.extend(toObject.call(this), {
			sala: this.sala
		});
	};
})(fabric.Object.prototype.toObject);