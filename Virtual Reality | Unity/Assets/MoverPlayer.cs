using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MoverPlayer : MonoBehaviour 
{
	private Rigidbody rb;
	private SphereCollider sc;

	public float speed;
	public GameObject camara;

	// Use this for initialization
	void Start () {
		rb = GetComponent<Rigidbody> ();
		sc = GetComponent<SphereCollider> ();
	}

	// Update is called once per frame
	void Update () {
		float anguloX = camara.transform.rotation.eulerAngles.x;

		if (anguloX <= 5 || anguloX >= 355 || (anguloX>=60 && anguloX <300)) {
			return;	// No avanza
		}

		Vector3 unitario = camara.transform.forward;    // La dirección hacia donde VE la cámara
		unitario.y = 0;
		Vector3 paso = unitario * (speed + (anguloX / 200));
		Vector3 nuevo = rb.position + paso;
		if (anguloX < 355 && anguloX >= 300) {
			paso = unitario * (speed + ((360 - anguloX) / 200));
			nuevo = rb.position - paso;
		}

		// Norte - CCI  -  Biblioteca,   ESTE  -  Torre cubículos  -  CEDETEC
		if (nuevo.z > 19 && nuevo.z < 35 && nuevo.x > 10 && nuevo.x < 22) {
			rb.position = nuevo;
		}
	}
}
